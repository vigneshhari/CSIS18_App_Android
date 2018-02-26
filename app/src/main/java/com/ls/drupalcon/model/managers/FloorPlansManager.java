package com.ls.drupalcon.model.managers;

import com.ls.drupal.AbstractBaseDrupalEntity;
import com.ls.drupal.AbstractDrupalByteEntity;
import com.ls.drupal.DrupalByteEntity;
import com.ls.drupal.DrupalClient;
import com.ls.drupal.DrupalImageEntity;
import com.ls.drupalcon.app.App;
import com.ls.drupalcon.model.dao.FloorPlanDao;
import com.ls.drupalcon.model.dao.LocationDao;
import com.ls.drupalcon.model.data.FloorPlan;
import com.ls.drupalcon.model.data.Location;
import com.ls.drupalcon.model.requests.FloorPlansRequest;
import com.ls.drupalcon.model.requests.LocationRequest;
import com.ls.http.base.BaseRequest;
import com.ls.http.base.ResponseData;
import com.ls.util.L;
import com.ls.utils.FileUtils;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class FloorPlansManager extends SynchronousItemManager<FloorPlan.Holder, Object, String> {

    private FloorPlanDao mFloorPlansDAO;
    public FloorPlansManager(DrupalClient client) {
        super(client);
        mFloorPlansDAO = new FloorPlanDao();
    }

    @Override
    protected AbstractBaseDrupalEntity getEntityToFetch(DrupalClient client, Object requestParams) {
        return new FloorPlansRequest(client);
    }

    @Override
    protected String getEntityRequestTag(Object params) {
        return "floorPlans";
    }

    @Override
    protected boolean storeResponse(FloorPlan.Holder requestResponse, String tag) {
        List<FloorPlan> plans = requestResponse.getFloorPlans();
        if (plans == null) {
            return false;
        }

        mFloorPlansDAO.saveOrUpdateDataSafe(plans);
        for (FloorPlan floor : plans) {
            if (floor != null) {
                if (!floor.isDeleted()){
                  if(!loadImageForFloor(floor)){
                      L.e("Image loading failed:" + floor.getImageURL());
                      return false;
                  }
                }
            }
        }

        for (FloorPlan floor : plans) {
            if (floor != null) {
                if (floor.isDeleted()) {
                    if(mFloorPlansDAO.deleteDataSafe(floor.getId()) > 0) {
                        FileUtils.deleteStoredFile(floor.getFilePath(), App.getContext());
                    }
                }
            }
        }

        return true;
    }

    public List<FloorPlan> getFloorPlans() {
        List<FloorPlan> result = mFloorPlansDAO.getAllSafe();
        Collections.sort(result);
        return result;
    }

    public Bitmap getImageForPlan(FloorPlan plan,int requiredWidth,int requiredHeight){
        Bitmap planImage = FileUtils.readBitmapFromStoredFile(plan.getFilePath(),requiredWidth, requiredHeight, App.getContext());
        return planImage;
    }

    public void clear() {
        mFloorPlansDAO.deleteAll();
    }

//    private boolean loadImageForFloor(FloorPlan floor){
//        //Load new image
//        DrupalImageEntity imageEntity = new DrupalImageEntity(getClient());
//        imageEntity.setImagePath(floor.getImageURL());
//        try {
//            imageEntity.pullFromServer(true, floor.getImageURL(), null);
//            Drawable imageDrawable = imageEntity.getManagedData();
//
//            //Store image
//            if (imageDrawable instanceof BitmapDrawable) {
//                Bitmap image = ((BitmapDrawable) imageDrawable).getBitmap();
//                return FileUtils.writeBitmapToStorage(floor.getFilePath(), image, App.getContext());
//            } else {
//                return false;
//            }
//        }catch (Error e){
//            return false;
//        }
//    }

    private boolean loadImageForFloor(final FloorPlan floor){
        //Load new image
        DrupalByteEntity imageEntity = new DrupalByteEntity(getClient())
        {
            @Override
            protected String getPath()
            {
                return floor.getImageURL();
            }

            @Override
            protected Map<String, String> getItemRequestPostParameters()
            {
                return null;
            }

            @Override
            protected Map<String, Object> getItemRequestGetParameters(BaseRequest.RequestMethod method)
            {
                return null;
            }
        };

        final CountDownLatch latch = new CountDownLatch(1);
        final ResponseResult result = new ResponseResult();

        try {
            imageEntity.pullFromServer(true, floor.getImageURL(), new AbstractBaseDrupalEntity.OnEntityRequestListener() {
                @Override
                public void onRequestCompleted(AbstractBaseDrupalEntity entity, Object tag, ResponseData data) {
                    byte[]imageData = (byte[]) data.getData();

                    //Store image
                    if (imageData != null && imageData.length > 0) {
                        result.isSuccessful = FileUtils.writeBytesToStorage(floor.getFilePath(), imageData, App.getContext());
                    } else {
                        result.isSuccessful = false;
                    }
                    latch.countDown();
                }

                @Override
                public void onRequestFailed(AbstractBaseDrupalEntity entity, Object tag, ResponseData data) {
                    result.isSuccessful = false;
                    latch.countDown();
                }

                @Override
                public void onRequestCanceled(AbstractBaseDrupalEntity entity, Object tag) {
                    result.isSuccessful = false;
                    latch.countDown();
                }
            });

            latch.await();
        } catch (InterruptedException e){
            e.printStackTrace();
        }

        return result.isSuccessful;
    }

    private class ResponseResult{
        public boolean isSuccessful;
    }
}
