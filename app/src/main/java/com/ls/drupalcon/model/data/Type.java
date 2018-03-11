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
    public static final int ENVIRONMENT = 15;
    public static final int HANDING_OVER = 16;
    public static final int AWARDS = 17;
    public static final int VOLUNTEER_1 = 18;
    public static final int SPECIAL_EVENT = 19;
    public static final int KITE = 20;
    public static final int VOLUNTEER_2 = 21;

    public static final int TEMPLE_1 = 22;
    public static final int TEMPLE_2 = 23;
    public static final int BUS = 24;
    public static final int STUPA = 25;
    public static final int HOT_SPRING = 26;
    public static final int UNIVERSITY = 27;
    public static final int MUESEUM = 28;




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
                return R.drawable.ic_clock;
            case Type.REGISTRATION:
                return R.drawable.ic_registration;
            case Type.LANG_CHALLENGE:
                return R.drawable.ic_lang_challenge ;
            case Type.LUNCH:
                 return R.drawable.ic_lunch;
            case Type.LIGHTING_LAMP:
                return R.drawable.ic_light_lamp;
            case Type.INTRODUCTION_EVENT:
                return R.drawable.ic_start;
            case Type.SPONSOR_SPACE:
                return R.drawable.ic_sponsor;
            case Type.TALK:
                return R.drawable.ic_talk;
            case Type.HI_TEA:
                return R.drawable.ic_tea;
            case Type.WORKSHOP:
                return R.drawable.ic_workshop;
            case Type.DINNER:
                return R.drawable.ic_dinner;
            case Type.INTERVIEWS:
                return R.drawable.ic_interview;
            case Type.BREAKFAST:
                return R.drawable.ic_breakfast;
            case Type.ENVIRONMENT:
                return R.drawable.ic_environment;
            case Type.HANDING_OVER:
                return R.drawable.ic_hand_over;
            case Type.VOLUNTEER_1:
                return R.drawable.ic_volunteer1;
            case Type.VOLUNTEER_2:
                return R.drawable.ic_volunteer2;
            case Type.AWARDS:
                return R.drawable.ic_award;
            case Type.SPECIAL_EVENT:
                return R.drawable.ic_special_event;
            case Type.KITE:
                return R.drawable.ic_kite;
            case Type.TEMPLE_1:
                return R.drawable.ic_temple;
            case Type.TEMPLE_2:
                return R.drawable.ic_temple2;
            case Type.BUS:
                return R.drawable.ic_bus;
            case Type.STUPA:
                return R.drawable.ic_stupa;
            case Type.HOT_SPRING:
                return R.drawable.ic_hot_spring;
            case Type.UNIVERSITY:
                return R.drawable.ic_university;
            case Type.MUESEUM:
                return R.drawable.ic_muesuem;
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
