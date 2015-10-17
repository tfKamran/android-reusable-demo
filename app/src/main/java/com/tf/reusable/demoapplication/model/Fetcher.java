package com.tf.reusable.demoapplication.model;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstract class to be implemented by any class that is designed to fetch data from a remote data source
 */
public abstract class Fetcher<T> {
    private List<RequestHandler> requestHandlers;

    /**
     * Interface definition for a callback invoked when a request is served
     * It should have a method with an appropriate data-type passed in to handle the onReceived event
     */
    protected interface OnReceivedListener<T> {
        void onReceived(T t);
    }

    /**
     * Provides a basic implementation of handling requests and cancellation of requests
     */
    public class RequestHandler<T> implements IRequestHandler<T> {
        private Context context;
        private OnReceivedListener listener;
        private AsyncTask asyncTask;

        public RequestHandler(Context context) {
            this.context = context;
        }

        public Context getContext() {
            return this.context;
        }

        public void sendRequest(OnReceivedListener listener, String url, T t) {
            if (requestHandlers == null) {
                requestHandlers = new ArrayList<>();
            }

            if (listener != null)
                requestHandlers.remove(this);

            this.listener = listener;

            requestHandlers.add(this);
        }

        public void cancelRequest() {
            requestHandlers.remove(this);

            try {
                if (asyncTask != null)
                    asyncTask.cancel(true);
            } catch (Exception e) { }
        }

        public void cancelAllRequests() {
            for (RequestHandler requestHandler : getRequestHandlers()) {
                try {
                    if (requestHandler.getAsyncTask() != null)
                        requestHandler.getAsyncTask().cancel(true);
                } catch (Exception e) { }
            }

            getRequestHandlers().clear();
        }

        public OnReceivedListener getOnReceivedListener() {
            return this.listener;
        }

        public List<RequestHandler> getRequestHandlers() {
            return Fetcher.this.requestHandlers;
        }

        public AsyncTask getAsyncTask() {
            return asyncTask;
        }

        public void setAsyncTask(AsyncTask asyncTask) {
            this.asyncTask = asyncTask;
        }
    }

    /**
     * Makes a request to the remote data source and registers the callback
     * @param context Context for which the request is being made
     * @param listener The callback that will be invoked with received data
     * @return see @RequestHandler
     */
    public IRequestHandler fetch(Context context, OnReceivedListener listener) {
        return fetch(context, listener, null, null);
    }

    /**
     * Makes a request to the remote data source and registers the callback
     * @param context Context for which the request is being made
     * @param listener The callback that will be invoked with received data
     * @param url The URL of the source to be requested
     * @return see @RequestHandler
     */
    public IRequestHandler fetch(Context context, OnReceivedListener listener, String url, T t) {
        RequestHandler requestHandler = new RequestHandler(context);

        requestHandler.sendRequest(listener, url, t);

        return requestHandler;
    }
}
