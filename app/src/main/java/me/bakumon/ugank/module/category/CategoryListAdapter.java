package me.bakumon.ugank.module.category;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import es.dmoral.toasty.Toasty;
import me.bakumon.ugank.ConfigManage;
import me.bakumon.ugank.R;
import me.bakumon.ugank.base.adapter.CommonAdapter4RecyclerView;
import me.bakumon.ugank.base.adapter.CommonHolder4RecyclerView;
import me.bakumon.ugank.base.adapter.ListenerWithPosition;
import me.bakumon.ugank.entity.CategoryResult;
import me.bakumon.ugank.entity.Favorite;
import me.bakumon.ugank.module.webview.WebViewActivity;
import me.bakumon.ugank.utills.DateUtil;
import me.bakumon.ugank.utills.DisplayUtils;

/**
 * CategoryListAdapter
 * Created by bakumon on 2016/10/13.
 */

public class CategoryListAdapter extends CommonAdapter4RecyclerView<CategoryResult.ResultsBean> implements ListenerWithPosition.OnClickWithPositionListener<CommonHolder4RecyclerView> {

    public CategoryListAdapter(Context context) {
        super(context, null, R.layout.item);
    }

    @Override
    public void convert(CommonHolder4RecyclerView holder, CategoryResult.ResultsBean androidResult) {
        if (androidResult != null) {
            ImageView imageView = holder.getView(R.id.iv_item_img);
            if (ConfigManage.INSTANCE.isListShowImg()) { // 列表显示图片
                imageView.setVisibility(View.VISIBLE);
                String quality = "";
                if (androidResult.images != null && androidResult.images.size() > 0) {
                    switch (ConfigManage.INSTANCE.getThumbnailQuality()) {
                        case 0: // 原图
                            quality = "?imageView2/0/w/400";
                            break;
                        case 1: //
                            quality = "?imageView2/0/w/280";
                            break;
                        case 2:
                            quality = "?imageView2/0/w/190";
                            break;
                    }
                    imageView.setVisibility(View.VISIBLE);
                    int size = DisplayUtils.dp2px(70, mContext);
                    Picasso.with(mContext)
                            .load(androidResult.images.get(0) + quality)
                            .resize(size, size)
                            .placeholder(R.mipmap.image_default)
                            .tag("Thumbnails_categoryList_item")
                            .config(Bitmap.Config.RGB_565)
                            .into(imageView);
                } else {
//                    Picasso.with(mContext).load(R.mipmap.image_default).into(imageView);
                    imageView.setVisibility(View.GONE);
                }
            } else { // 列表不显示图片
                imageView.setVisibility(View.GONE);
            }
            holder.setTextViewText(R.id.tv_item_title, androidResult.desc == null ? "unknown" : androidResult.desc);
            holder.setTextViewText(R.id.tv_item_publisher, androidResult.who == null ? "unknown" : androidResult.who);
            holder.setTextViewText(R.id.tv_item_time, DateUtil.dateFormat(androidResult.publishedAt));
            holder.setOnClickListener(this, R.id.ll_item);
        }
    }

    @Override
    public void onClick(View v, int position, CommonHolder4RecyclerView holder) {
        if (mData == null || mData.get(position) == null) {
            Toasty.error(mContext, "数据异常").show();
            return;
        }
        Intent intent = new Intent(mContext, WebViewActivity.class);
        intent.putExtra(WebViewActivity.GANK_TITLE, mData.get(position).desc);
        intent.putExtra(WebViewActivity.GANK_URL, mData.get(position).url);
        Favorite favorite = new Favorite();
        favorite.setAuthor(mData.get(position).who);
        favorite.setData(mData.get(position).publishedAt);
        favorite.setTitle(mData.get(position).desc);
        favorite.setType(mData.get(position).type);
        favorite.setUrl(mData.get(position).url);
        favorite.setGankID(mData.get(position)._id);
        intent.putExtra(WebViewActivity.FAVORITE_DATA, favorite);
        mContext.startActivity(intent);
    }
}
