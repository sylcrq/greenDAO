package de.greenrobot.daogenerator.gentest;

import java.io.IOException;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * Java application
 *
 * Created by shenyunlong on 2015/8/21.
 */
public class MyDaoTestGenerator {


    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1024, "com.example.syl.mydaotest");

        Entity weather = schema.addEntity("Weather");
        weather.addIdProperty();
        weather.addIntProperty("temp");
        weather.addStringProperty("weather");
        weather.addDateProperty("date");
        weather.addStringProperty("date_str");

        // TODO: 相对路径
        new DaoGenerator().generateAll(schema, "E:/greenDAO/MyDaoTest/src-gen");
    }



}
