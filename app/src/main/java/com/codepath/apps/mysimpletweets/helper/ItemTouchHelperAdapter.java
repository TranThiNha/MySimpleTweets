package com.codepath.apps.mysimpletweets.helper;

/**
 * Created by MyPC on 11/17/2016.
 */
public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}
