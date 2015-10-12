package com.codepath.photoviewer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by msamant on 10/7/15.
 */
public class InstaPhotosAdapter extends ArrayAdapter<InstaPhoto> {

    public InstaPhotosAdapter(Context context, List<InstaPhoto> objects) {
        super(context, android.R.layout.simple_expandable_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        InstaPhoto photo = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }
        // Lookup view for data population
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        ImageView ivPhoto = (ImageView)convertView.findViewById(R.id.ivPhoto);
        ImageView ivProfilePic = (ImageView)convertView.findViewById(R.id.ivUserPhoto);
        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
        TextView tvNumOfLikes = (TextView)convertView.findViewById(R.id.tvNumOfLikes);
        TextView tvUname = (TextView)convertView.findViewById(R.id.tvUname);
        TextView tvUnameCmnt1 = (TextView)convertView.findViewById(R.id.tvComment_uname1);
        TextView tvComment1 = (TextView)convertView.findViewById(R.id.tvComment1);
        TextView tvUnameCmnt2 = (TextView)convertView.findViewById(R.id.tvComment_uname2);
        TextView tvComment2 = (TextView)convertView.findViewById(R.id.tvComment2);
        TextView tvTimeAgo = (TextView)convertView.findViewById(R.id.tvTimeAgo);

        tvCaption.setText(photo.caption);
        tvUserName.setText(photo.username);
        tvNumOfLikes.setText(""+photo.likeCounts);
        tvUname.setText(photo.username);
        tvUnameCmnt1.setText(photo.uname_comment1);
        tvComment1.setText(photo.comment1);
        tvUnameCmnt2.setText(photo.uname_comment2);
        tvComment2.setText(photo.comment2);
        tvTimeAgo.setText(photo.timeAgo);
        ivProfilePic.setImageResource(0);
        ivPhoto.setImageResource(0);
        Picasso.with(getContext()).load(photo.imgUrl).fit().placeholder(R.drawable.abc_spinner_mtrl_am_alpha).into(ivPhoto);
        Picasso.with(getContext()).load(photo.profilePic).fit().placeholder(R.drawable.abc_spinner_mtrl_am_alpha).transform(new CircleTransform()).into(ivProfilePic);
        // Return the completed view to render on screen
        return convertView;
    }

}
