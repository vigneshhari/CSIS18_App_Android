package com.ls.drupalcon.model.data;

import com.google.gson.annotations.SerializedName;

import com.ls.drupalcon.R;
import com.ls.drupalcon.model.database.AbstractEntity;
import com.ls.utils.CursorParser;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class Type extends AbstractEntity<Long> {

    public static final int NONE = 1;
    public static final int REPORTING_TIME = 2;
    public static final int REGISTRATION = 3;
    public static final int LANG_CHALLENGE = 4;
    public static final int LUNCH = 5;
    public static final int LIGHTING_LAMP = 6;
    public static final int INTRODUCTION_EVENT = 7;
    public static final int SPONSOR_SPACE = 8;
    public static final int TALK = 9;
    public static final int HI_TEA = 10;
    public static final int WORKSHOP = 11;
    public static final int DINNER = 12;
    public static final int INTERVIEWS = 13;
    public static final int BREAKFAST = 14;



    @SerializedName("typeId")
    private long mId;

    @SerializedName("typeName")
    private String mName;

    @SerializedName("typeIconURL")
    private String mIconUrl;

    @SerializedName("order")
    private double mOrder;

    @SerializedName("deleted")
    private boolean mDeleted;

    @Override
    public ContentValues getContentValues() {
        ContentValues result = new ContentValues();
        result.put("_id", mId);
        result.put("name", mName);
        result.put("type_icon_url", mIconUrl);
        result.put("_order", mOrder);
        result.put("_deleted", mDeleted);

        return result;
    }

    @Override
    public void initialize(Cursor theCursor) {
        CursorParser parser = new CursorParser(theCursor);

        mId = parser.readLong();
        mName = parser.readString();
        mIconUrl = parser.readString();
        mOrder = parser.readDouble();
        mDeleted = parser.readBoolean();
    }

    @Override
    public Long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setDeleted(boolean mDeleted) {
        this.mDeleted = mDeleted;
    }

    public boolean isDeleted() {
        return mDeleted;
    }

    public String getIconUrl() {
        return mIconUrl;
    }

    public void setIconUrl(String iconUrl) {
        mIconUrl = iconUrl;
    }

    public static int getIcon(long typeId) {
        switch ((int) typeId) {

            case Type.REPORTING_TIME:
                return R.drawable.ic_program_speach_of_the_day;

            case Type.REGISTRATION:
                return R.drawable.ic_program_speach_of_the_day;


            default:
                return 0;
        }
    }

    public static class Holder {

        @SerializedName("types")
        private List<Type> mTypes = new ArrayList<Type>();

        public List<Type> getTypes() {
            return mTypes;
        }
    }

}
