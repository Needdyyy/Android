package com.needyyy.app.manager.APIManager;

import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.needyyy.app.BuildConfig;
import com.needyyy.app.constants.Blocks.Block;
import com.needyyy.app.constants.Blocks.GenricResponse;
import com.needyyy.app.constants.Constants;
import com.needyyy.app.manager.BaseManager.BaseManager;

import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by surya on 15/5/17.
 * copyright to : surya
 */

public class APIManager extends BaseManager implements Constants {

    public static int isupdated = 0;

    //API Request Content Types
    private static final String kContentType                = "Content-Type";
    private static final String kContentTypeText            = "text/html";
    private static final String kContentTypeJSON            = "application/json; charset=utf-8";
    private static final String kContentTypeFormData        = "application/x-www-form-urlencoded";
    private static final String kContentTypeMultiPart       = "multipart/form-data";
    private static final String kContentTypeRawJson         = "application/json";
    private static final String kContentTypeImage           = "image/jpeg";
    private static final String kDefaultImageName           = "photo.jpg";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static APIManager       _APIManager;
    private Retrofit                _Retrofit;
    private APIRequestHelper        _APIHelper;

    /**
     * a private constructor to prevent any other class from initiating
     */
    private APIManager() {

    }

    /**
     * Singleton instance of {@link APIManager}
     *
     * @return a thread safe singleton object of {@link APIManager}
     */
    public static synchronized APIManager APIManager() {
        if (_APIManager == null) {
            _APIManager = new APIManager();
//            httpClient.addInterceptor(new Interceptor() {
//                @Override
//                public Response intercept(Chain chain) throws IOException {
//                    Request original = chain.request();
//                    String authrization;
//                    LoggedInUser user = BaseManager.getDataFromPreferences(kCurrentUser, LoggedInUser.class);
//                    if (user != null)
//                        authrization = user.getToken();
//                    else
//                        authrization = "";
//
//                    Request.Builder requestBuilder = original.newBuilder()
//                            .header("Content-Type", "application/json")
//                            .header("Authorization", authrization)
//                            .method(original.method(), original.body());
//
//                    Request request = requestBuilder.build();
//                    return chain.proceed(request);
//                }
//            });
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient okHttpClient = httpClient.addInterceptor(interceptor)
                    .connectTimeout(25, TimeUnit.SECONDS)
                    .readTimeout(25, TimeUnit.SECONDS)
                    .writeTimeout(25, TimeUnit.SECONDS)
                    .build();

            final Gson gson0 = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create();

            _APIManager._Retrofit = new Retrofit.Builder()
                    .baseUrl(kAPIBaseURL)
                    .addConverterFactory(GsonConverterFactory.create(gson0))
                    .client(okHttpClient)
                    .build();

            _APIManager._APIHelper = _APIManager._Retrofit.create(APIRequestHelper.class);
        }
        return _APIManager;
    }

    /**
     * method converts parameter into  {@link retrofit2.http.Multipart} multipart/form-data
     *
     * @param parameter map which contains request body
     * @return {@link RequestBody} for mutipart request : multipart/form-data
     */
    private RequestBody requestBody(String parameter) {
        return RequestBody.create(okhttp3.MediaType.parse(kContentTypeMultiPart), parameter);
    }

    /**
     * method convert parameter into json for application/json; charset=utf-8 type of request
     *
     * @param parameter map which contains request body
     * @return {@link RequestBody} for raw request e.g application/json; charset=utf-8
     */
    private RequestBody requestRawBody(Map<String, Object> parameter) {
        return RequestBody
                .create(okhttp3.MediaType.parse(kContentTypeJSON), new JSONObject(parameter).toString());
    }

    /**
     * method to process raw request, should be used only when theres no file included with the
     * request parameters
     *
     * @param APIKey     = api key to called
     * @param parameters = request parameters
     * @param success    = success Block
     * @param failure    = failure Block
     */
    public void processRawRequest(String APIKey, HashMap<String, Object> parameters,
                                  Block.Success<JSONObject> success,
                                  Block.Failure failure) {

        final Call<JsonObject> request = getAPIRawRequest(APIKey, parameters);
        apiRequestWithAPI(request, success, failure);
    }

    /**
     * method to process raw request for get Method
     *
     * @param APIKey     = api key to called with parameters
     * @param success    = success Block
     * @param failure    = failure Block
     */
    public void processRawRequestForGetMethod(String APIKey,
                                              Block.Success<JSONObject> success,
                                              Block.Failure failure) {

        final Call<JsonObject> request = getAPIRawRequestGetMethod(APIKey);
        apiRequestWithAPI(request, success, failure);
    }

    private HashMap<String, String> getHeader() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("Content-Type", "application/json");
        return hashMap;
    }

    public static String bodyToString(final Request request) {
        try {


            final Request copy = request.newBuilder().build();
//            if(copy.body() == null) {
//                return kEmptyString;
//            }

            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    private static void printAPILogs(final Request finalRequest, final JsonObject response) {

        try {

            StringBuilder requestLog = new StringBuilder("URL: " + finalRequest.url().toString());

            requestLog.append("%nMETHOD: " + finalRequest.method());

            if(finalRequest.body() != null)   {
                requestLog.append("%nCONTENT TYPE: " + finalRequest.body().contentType());
                requestLog.append("%nBODY: " + bodyToString(finalRequest));
            }
            Log.d("APIREQUEST %s", String.valueOf(requestLog));

            Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
            String json = gson.toJson(response);

            Log.d("RESPONSE %s", json);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * base method to get data from server
     *
     * @param request a retrofit {@link Call} containing parameters
     * @param success Block to be executed for success condition
     * @param failure Block to be executed for failure condition
     */
    private void apiRequestWithAPI(Call<JsonObject> request, Block.Success<JSONObject> success,
                                   Block.Failure failure) {
        try {

            JsonObject response = request.execute().body();

            if (BuildConfig.DEBUG) {
                printAPILogs(request.request(), response);
            }

            if (response != null) {
//                checkStatus(response, (Status iStatus, GenricResponse<JSONObject> genricResponse) -> {
//                    //if success, it return JSONObject if fail, it return message
//                    success.iSuccess(iStatus, genricResponse);
//                }, (Status iStatus, String message) -> {
//                    //If failure occurred.
//                    failure.iFailure(Status.fail, message);
//                });
            } else {
                failure.iFailure(Status.fail, kMessageServerNotRespondingError);
            }
        } catch (Exception e) {
            e.printStackTrace();
            failure.iFailure(Status.fail, kSocketTimeOut);
        }
    }

    private Boolean isError(String status, String message) {
        if (message == null || message == "null" || status == "OK" || status == "UserExists") {
            return false;
        } else {
            return true;
        }
    }

    private String getStatusMessage(HTTPStatus status) {
        String errorMessage = null;

        if (status == HTTPStatus.success)    {
            errorMessage = null;
        }
        else if(status == null)    {
            errorMessage = "Error. Please try again.";
        }
        else if (status == HTTPStatus.userExists)  {
            errorMessage = "User already exists!";
        }
        else if(status == HTTPStatus.missingInput)    {
            errorMessage = "There are some missing inputs!";
        }
        else  if (status == HTTPStatus.invalidInputs) {
            errorMessage = "There are some invalid inputs!";
        }
        else  if (status == HTTPStatus.unknownError) {
            errorMessage = "Some error has occured!";
        }
        else  if (status == HTTPStatus.userNotExist) {
            errorMessage = "User doesn't exist!";
        }
        else  if (status == HTTPStatus.failueAuthLogin) {
            errorMessage = "Login Failed!";
        }
        else  if (status == HTTPStatus.noActiveRequest) {
            errorMessage = "There are no active Requests!";
        }
        else  if (status == HTTPStatus.codeNotvalid) {
            errorMessage = "Invalid code";
        }
        else  if (status == HTTPStatus.noRecordFound || status == HTTPStatus.noRegisteredContactFound) {
            errorMessage = "No record found";
        }
        else  {
            errorMessage = "Something's not right. Please try again.";
        }

        return  errorMessage;
    }

    /**
     * checkStatus is responsible to check whether api return the desired result or not by check
     * the api status. If status is success then it returns JSONObject other wise return a
     * suitable message.
     *
     * @param response json response result
     * @param success  Block to be executed for success condition
     * @param failure  Block to be executed for success condition
     */
    private void checkStatus(JsonObject response, Block.Success<JSONObject> success,
                             Block.Failure failure) {
        try {
            JSONObject jsonResposne = new JSONObject(response.toString());
            GenricResponse<JSONObject> genricResponse = new GenricResponse<>(jsonResposne);

            HTTPStatus status = HTTPStatus.getStatus(jsonResposne.getString(kStatus));
            String message = jsonResposne.getString(kErrorMessage);

            if (isError(status.getValue(), message)) {
                if (message == null || message.equals("null") || message.length() == 0) {
                    failure.iFailure(Status.fail, getStatusMessage(status));
                } else {
                    failure.iFailure(Status.fail, message);
                }
            } else {
                if(status == HTTPStatus.noRecordFound)  {
                    success.iSuccess(Status.noRecordFound, genricResponse);
                }
                else    {
                    success.iSuccess(Status.success, genricResponse);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            failure.iFailure(Status.fail, kMessageInternalInconsistency);
        }
    }

    /**
     * Create API request with APIKey and corresponding parameters. This method will work for all
     * cases. 1. To upload multiple file to server. 2. To get data from server in multiPart format.
     *
     * @param APIKey     contains api key that to be called
     * @param parameters to be include as body to the request
     * @return return a {@link retrofit2.http.Multipart} request with the type of form-data
     */
    private Call<JsonObject> getAPIRequest(String APIKey, HashMap<String, Object> parameters) {
        //Process the parameters as per the File and details. If object is of File type then it create MultipartBody.Part and store it in fileList. Else object will be store in detailMap. detailMap and fileList will be use to create APIRequest.
        List<MultipartBody.Part> fileList = new ArrayList<>();
        HashMap<String, RequestBody> detailMap = new HashMap<>();
        for (String key : parameters.keySet()) {
            if (key != null && !key.equals(kEmptyString)) {
                Object value = parameters.get(key);
                if (value.getClass() == File.class) {
                    File file = new File(value.toString());
                    if (file.exists()) {
                        // create RequestBody instance from file
                        RequestBody requestFile = RequestBody
                                .create(okhttp3.MediaType.parse(kContentTypeImage), file);
                        // MultipartBody.Part is used to send also the actual file name
                        MultipartBody.Part body = MultipartBody.Part
                                .createFormData(key, kDefaultImageName, requestFile);
                        fileList.add(body);
                    }
                } else if (value.getClass() == HashMap.class) {
                    // Initialize Builder (not RequestBody)
                    FormBody.Builder builder = new FormBody.Builder();
                    // Add Params to Builder
                    for (Map.Entry<String, Object> entry : ((HashMap<String, Object>) value).entrySet()) {
                        builder.add(entry.getKey(), entry.getValue().toString());
                    }
                    // Create RequestBody
                    RequestBody formBody = builder.build();
                    detailMap.put(key, formBody);
                } else {
                    detailMap.put(key, requestBody(parameters.get(key).toString()));
                }
            }
        }

        return _APIHelper.APIRequestWithFile(APIKey, detailMap, fileList);
    }

    /**
     * method to make convert the call into a raw one with type application/json
     *
     * @param APIKey     that to be called
     * @param parameters to be include as body to the request
     * @return a retrofit {@link Call} with content type application/json; charset=utf-8
     */
    private Call<JsonObject> getAPIRawRequest(String APIKey, Map<String, Object> parameters) {
        RequestBody body = requestRawBody(parameters);
        Map<String, String> headerMap = getHeader();
        return _APIHelper.APIRequestRaw(APIKey, headerMap, body);
    }

    private Call<JsonObject> getAPIRawRequestGetMethod(String APIKey) {
        return _APIHelper.APIRequestRawForGetMethod(APIKey);
    }
}