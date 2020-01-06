package com.needyyy.app.manager.ModelManager;

import android.text.TextUtils;


import com.needyyy.app.constants.Blocks.Block;
import com.needyyy.app.constants.Blocks.GenricResponse;
import com.needyyy.app.constants.Constants;
import com.needyyy.app.libraries.dispatchQueue.DispatchQueue;
import com.needyyy.app.manager.APIManager.APIManager;
import com.needyyy.app.manager.APIManager.APIRequestHelper;
import com.needyyy.app.manager.BaseManager.BaseManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;




/**
 * Created by surya on 21/4/17.
 * Singleton class to manage all models in projects. this is basically provide data to view in the
 * form of models
 */

public class ModelManager extends BaseManager implements Constants {

    private final static String TAG = ModelManager.class.getSimpleName();
    //Static Properties
    private static ModelManager         _ModelManager;

    private LoggedInUser                mCurrentUser;

    //This realm is use to update the realm object in background thread to update Database after API response.

    //Instance Properties
    private DispatchQueue dispatchQueue =
            new DispatchQueue("com.queue.serial.modelmanager", DispatchQueue.QoS.userInitiated);


    /**
     * private constructor make it to be Singleton class
     */
    private ModelManager() {
        mCurrentUser = getDataFromPreferences(kCurrentUser, LoggedInUser.class);
    }

    /**
     * method to create a threadsafe singleton class instance
     *
     * @return a thread safe singleton object of model manager
     */
    public static synchronized ModelManager modelManager() {
        if (_ModelManager == null) {
            _ModelManager = new ModelManager();
        }
        return _ModelManager;
    }

    public DispatchQueue getDispatchQueue() {
        return dispatchQueue;
    }

    public void archiveCurrentUser() {
        saveDataIntoPreferences(mCurrentUser, kCurrentUser);
    }

    /**
     * to initialize the singleton object
     */
    public void initializeModelManager() {
        System.out.println("ModelManager object start to initialized.");
//        dispatchQueue.async(() ->   {
//        });
    }

    public LoggedInUser getCurrentUser() {
        return this.mCurrentUser;
    }

   }
