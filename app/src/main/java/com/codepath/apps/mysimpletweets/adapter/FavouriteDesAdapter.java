package com.codepath.apps.mysimpletweets.adapter;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.Fragment.FavouriteDestinationFragment;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.helper.ItemTouchHelperAdapter;
import com.codepath.apps.mysimpletweets.helper.ItemTouchHelperViewHolder;
import com.codepath.apps.mysimpletweets.helper.OnStartDragListener;
import com.codepath.apps.mysimpletweets.models.Destination;
import com.codepath.apps.mysimpletweets.models.Tweet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by MyPC on 11/16/2016.
 */
public class FavouriteDesAdapter extends RecyclerView.Adapter<FavouriteDesAdapter.FavouriteDesViewHolder>
        implements ItemTouchHelperAdapter {
    List<Tweet> mDes;
    private OnItemClickListener mOnItemClickListener;
    private final OnStartDragListener mDragStartListener;

    public FavouriteDesAdapter(OnStartDragListener onStartDragListener){
        mDes = new ArrayList<>();
        mDragStartListener = onStartDragListener;
    }

    @Override
    public FavouriteDesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favourite_destination,parent,false);
        return new FavouriteDesViewHolder(itemView);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mDes, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        mDes.remove(position);
        notifyDataSetChanged();
        notifyItemRemoved(position);

    }

    public interface OnItemClickListener{
        void onClick(Tweet des);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mOnItemClickListener = listener;
    }

    public void addDes(List<Tweet> destinations){
        mDes.addAll(destinations);
        notifyItemRangeInserted(mDes.size(),destinations.size());
    }

    public void addDes(Tweet destination){
        mDes.add(destination);
        notifyItemRangeInserted(mDes.size(),1);
    }

    public void setDesS(List<Tweet> destinations){
        mDes.clear();
        mDes.addAll(destinations);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final FavouriteDesViewHolder holder, int position) {

        Tweet destination = mDes.get(position);
        holder.tvDes.setText(destination.getUser().getScreenName());
        holder.tvAddress.setText(destination.getCreateAt());
        // Start a drag whenever the handle view it touched
        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDes.size();
    }

    class FavouriteDesViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        TextView tvDes;
        TextView tvAddress;

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }

       public FavouriteDesViewHolder(final View itemView) {
           super(itemView);
           tvDes = (TextView) itemView.findViewById(R.id.tvDestination);
           tvAddress = (TextView) itemView.findViewById(R.id.tvAddress);
           itemView.setOnClickListener(new View.OnClickListener() {

               @Override
               public void onClick(View view) {
                   int position = getAdapterPosition();
                   Tweet destination = mDes.get(position);
                   if (mOnItemClickListener!= null){
                       mOnItemClickListener.onClick(destination);
                   }
               }
           });

           itemView.setOnLongClickListener(new View.OnLongClickListener() {
               @Override
               public boolean onLongClick(View view) {
                   final int position = getAdapterPosition();
                   new AlertDialog.Builder(itemView.getContext())
                           .setTitle("Delete Favourite Destination")
                           .setMessage("Are you sure you want to delete this destiantion?")
                           .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int which) {
                                   mDes.remove(position);
                                   notifyDataSetChanged();
                                   Snackbar.make(itemView,"UNDO", Snackbar.LENGTH_INDEFINITE).show();                               }
                           })
                           .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int which) {
                                   //do nothing
                               }
                           })
                           .setIcon(android.R.drawable.ic_dialog_alert)
                           .show();
                   return true;
               }
           });

       }
   }

}
