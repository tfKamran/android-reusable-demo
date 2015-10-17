package com.tf.reusable.demoapplication.model;

import android.content.Context;

/**
 * RequestHandler should be returned in response of any calls made to a remote data source to maintain the standard methods of sending requests and cancelling the same at any point of time
 */
public interface IRequestHandler<T> {
    /**
     * Makes a request to the remote data source and invokes the appropriate listeners on successful reception
     * @param listener The callback that will be invoked with received data
     * @param url The URL of the source to be requested
     */
    void sendRequest(Fetcher.OnReceivedListener listener, String url, T t);

    /**
     * Cancel the current request
     */
    void cancelRequest();

    /**
     * Cancel all previously made requests
     */
    void cancelAllRequests();

    /**
     * Returns the registered callback
     * @return The registered callback
     */
    Fetcher.OnReceivedListener getOnReceivedListener();

    /**
     * Returns context used to make requests
     * @return Context
     */
    Context getContext();
}
