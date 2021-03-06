package com.zhl.channeltagview.adapter;

import android.content.Context;

import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.donkingliang.groupedadapter.holder.BaseViewHolder;
import com.zhl.channeltagview.R;
import com.zhl.channeltagview.bean.ChannelItem;
import com.zhl.channeltagview.bean.GroupItem;
import com.zhl.channeltagview.view.ChannelTagView;

import java.util.ArrayList;

import cn.bingoogolapple.badgeview.BGABadgeTextView;
import cn.bingoogolapple.badgeview.BGABadgeable;
import cn.bingoogolapple.badgeview.BGADragDismissDelegate;

/**
 * 这是普通的分组Adapter 每一个组都有头部、尾部和子项。
 */
public class GroupedListAdapter extends GroupedRecyclerViewAdapter {

    private ArrayList<GroupItem> mGroups;
    private boolean openCategory;
    private ChannelTagView.RedDotRemainderListener redDotRemainderListener;

    public GroupedListAdapter(Context context, ArrayList<GroupItem> groups,boolean openCategory) {
        super(context);
        mGroups = groups;
        this.openCategory = openCategory;
    }

    @Override
    public int getGroupCount() {
        return mGroups == null ? 0 : mGroups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<ChannelItem> children = mGroups.get(groupPosition).getChannelItems();
        return children == null ? 0 : children.size();
    }

    @Override
    public boolean hasHeader(int groupPosition) {
        return openCategory;
    }

    @Override
    public boolean hasFooter(int groupPosition) {
        return false;
    }

    @Override
    public int getHeaderLayout(int viewType) {
        return R.layout.item_mulite_banner;
    }

    @Override
    public int getFooterLayout(int viewType) {
        return 0;
    }

    @Override
    public int getChildLayout(int viewType) {
        return R.layout.item_channel_view;
    }

    @Override
    public void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition) {
        GroupItem entity = mGroups.get(groupPosition);
        holder.setText(R.id.banner_title, entity.category);
    }

    @Override
    public void onBindFooterViewHolder(BaseViewHolder holder, int groupPosition) {

    }

    @Override
    public void onBindChildViewHolder(BaseViewHolder holder, int groupPosition, int childPosition) {
        ChannelItem entity = mGroups.get(groupPosition).getChannelItems().get(childPosition);
        holder.setText(R.id.item_tv, entity.title);
        final BGABadgeTextView title = (BGABadgeTextView) holder.get(R.id.item_tv);
        int pos = getPositionForChild(groupPosition,childPosition);
        final int position  = openCategory?pos - (groupPosition + 1) : pos;
        if (redDotRemainderListener.showUnAddedChannelBadge(title, position)) {
            title.setDragDismissDelegage(new BGADragDismissDelegate() {
                @Override
                public void onDismiss(BGABadgeable badgeable) {
                    redDotRemainderListener.OnDragDismiss(title, position);
                }
            });
            redDotRemainderListener.handleUnAddedChannelReddot(title, position);
        } else {
            title.hiddenBadge();
        }

    }

    public void setRedDotRemainderListener(ChannelTagView.RedDotRemainderListener redDotRemainderListener) {
        this.redDotRemainderListener = redDotRemainderListener;
    }

    public void setOpenCategory(boolean openCategory) {
        this.openCategory = openCategory;
        this.changeDataSet();
    }
}
