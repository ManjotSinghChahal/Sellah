package com.app.admin.sellah.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.admin.sellah.controller.WebServices.ApisHelper;
import com.app.admin.sellah.controller.utils.HelperPreferences;
import com.app.admin.sellah.model.extra.commentModel.Result;
import com.app.admin.sellah.view.CustomDialogs.EditCommentDialog;
import com.app.admin.sellah.view.CustomDialogs.S_Dialogs;
import com.app.admin.sellah.view.activities.ImageViewerActivity;
import com.bumptech.glide.Glide;
import com.app.admin.sellah.R;
import com.app.admin.sellah.model.extra.commentModel.CommentModel;
import com.app.admin.sellah.controller.utils.Global;
import com.app.admin.sellah.controller.utils.SAConstants;
import com.app.admin.sellah.view.fragments.PersonalProfileFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.app.admin.sellah.controller.utils.Global.getUser.isLogined;
import static com.app.admin.sellah.controller.utils.Global.makeTextViewResizable;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;

public class ProductInfoCommentAdapter extends RecyclerView.Adapter<ProductInfoCommentAdapter.ViewHolder> {
    CommentModel items;
    Context context;
    FragmentManager manager;
    String productId;
    CommentCallbacks callbacks;
    private PopupMenu popup;
    boolean seeall;
    CommentModel model;
    List<Result> list = new ArrayList<>();


    public ProductInfoCommentAdapter(Context con, boolean seealll, CommentModel items, FragmentManager manager, String productId, CommentCallbacks callbacks) {
        this.model = items;
        context = con;
        this.manager = manager;
        this.productId = productId;
        this.callbacks = callbacks;
        this.seeall = seealll;
        this.items = model;

//        if (items.getResult().size() < 2) {
//            this.items = model;
//        } else {
//
//            for (int i = 0; i < 2; i++) {
//
//                list.add(model.getResult().get(i));
//                this.items = new CommentModel();
//                this.items.setResult(list);
//
//            }
//
//        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comments_recycler, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


//            seeall.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                        items.setResult(model.getResult());
//                        notifyDataSetChanged();
//
//                }
//            });
        holder.profile.setText(items.getResult().get(position).getUsername());
        holder.ptime.setText(Global.getTimeAgo(Global.convertUTCToLocal(items.getResult().get(position).getCreatedAt())));
        holder.desc.setText(items.getResult().get(position).getComment());
//        if(holder.desc.getLineCount()>0){


        holder.desc.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                if (holder.desc.getLineCount() > 3) {
                    makeTextViewResizable(holder.desc, 3, ".. See More", true);
                    holder.desc.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }


            }
        });

        if (!items.getResult().get(position).getCommentLikeCount().equalsIgnoreCase("0")) {
            holder.relLikeCount.setVisibility(View.VISIBLE);
            holder.txtLikeCount.setText(items.getResult().get(position).getCommentLikeCount());
            if (items.getResult().get(position).getIsLiked().equalsIgnoreCase("y")) {
                holder.imgLike.setImageResource(R.drawable.like);
            } else {
                holder.imgLike.setImageResource(R.drawable.thumb_icon);
            }
        } else {
            holder.relLikeCount.setVisibility(View.GONE);
        }
//        makeTextViewResizable(holder.desc, 3, "... See More", true);
//        }
        /*holder.image.setImageResource(items.get(position).getImage());*/


        Glide.with(context)
                .load(items.getResult().get(position).getImage())
                .apply(Global.getGlideOptions())
                .into(holder.image);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new PersonalProfileFragment(), position);
            }
        });

        holder.imgCommentOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptions(holder, position);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showOptions(holder, position);
                return false;
            }
        });

        if (items.getResult().get(position).getCommentType().equalsIgnoreCase("img")) {
            holder.desc.setVisibility(View.GONE);
            holder.cardCommentImage.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(items.getResult().get(position).getCommentImage())
                    .apply(Global.getGlideOptions())
                    .into(holder.imgCommentImage);

            holder.cardCommentImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageViewerActivity.start(context, items.getResult().get(position).getCommentImage());
                }
            });
        } else {
            holder.desc.setVisibility(View.VISIBLE);
            holder.cardCommentImage.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        if (!seeall) {
            if (items.getResult().size() > 2) {
                return 2;
            }
        } else {
            return items.getResult().size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public ImageView imgCommentOptions, imgCommentImage, imgLike;
        public TextView profile, ptime, desc, txtLikeCount;
        RelativeLayout relLikeCount;
        CardView cardCommentImage;

        //        public TextView profile
        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.profile_image);
            imgCommentOptions = (ImageView) itemView.findViewById(R.id.img_comment_options);
            imgCommentImage = (ImageView) itemView.findViewById(R.id.img_comment_image);
            profile = (TextView) itemView.findViewById(R.id.profile_name);
            ptime = (TextView) itemView.findViewById(R.id.posted_time);
            desc = (TextView) itemView.findViewById(R.id.comment_desc);
            txtLikeCount = itemView.findViewById(R.id.count_tv);
            relLikeCount = itemView.findViewById(R.id.rel_likecount);
            imgLike = itemView.findViewById(R.id.img_like);
            cardCommentImage = itemView.findViewById(R.id.card_comment_image);
        }
    }

    public boolean loadFragment(Fragment fragment, int pos) {

        if (fragment != null) {
            Bundle bundle = new Bundle();
            bundle.putString(SAConstants.Keys.OTHER_USER_ID, items.getResult().get(pos).getUserId());
            fragment.setArguments(bundle);
            manager.beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack(null).commit();
            return true;
        }
        return false;
    }

    private void showOptions(ViewHolder holder, int position) {
        popup = new PopupMenu(context, holder.imgCommentOptions);
        MenuInflater inflater = popup.getMenuInflater();
        if (items.getResult().get(position).getUserId().equalsIgnoreCase(HelperPreferences.get(context).getString(UID))) {
            inflater.inflate(R.menu.menu_options_comment_owner, popup.getMenu());
            if (!items.getResult().get(position).getCommentType().equalsIgnoreCase("img")) {
                popup.getMenu().findItem(R.id.menu_edit_comment).setVisible(true);
            } else {
                popup.getMenu().findItem(R.id.menu_edit_comment).setVisible(false);
            }

        } else if (productId.equalsIgnoreCase(HelperPreferences.get(context).getString(UID))) {
            inflater.inflate(R.menu.menu_options_comment_user, popup.getMenu());
        } else {
            inflater.inflate(R.menu.menu_option_comment, popup.getMenu());
        }
        if (items.getResult().get(position).getIsLiked().equalsIgnoreCase("y")) {
            popup.getMenu().findItem(R.id.menu_like).setVisible(false);
            popup.getMenu().findItem(R.id.menu_unlike).setVisible(true);
        } else {
            popup.getMenu().findItem(R.id.menu_like).setVisible(true);
            popup.getMenu().findItem(R.id.menu_unlike).setVisible(false);
        }

        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                if (isLogined(context)) {
                    switch (menuItem.getItemId()) {
                        case R.id.menu_like:
                            new ApisHelper().likeCommentApi(context, items.getResult().get(position).getCommentId(), "y", new ApisHelper.LikeCommentCallback() {
                                @Override
                                public void onLikeCommentSuccess(String msg) {
                                    Snackbar.make(((Activity) context).getWindow().getDecorView(), msg, Snackbar.LENGTH_SHORT)
                                            .setAction("", null).show();
                                    items.getResult().get(position).setIsLiked("Y");
                                    items.getResult().get(position).setCommentLikeCount(String.valueOf((Integer.parseInt(items.getResult().get(position).getCommentLikeCount()) + 1)));
                                    notifyDataSetChanged();
                                }

                                @Override
                                public void onLikeCommentFailure() {
                                    Snackbar.make(((Activity) context).getWindow().getDecorView(), "Something went's wrong", Snackbar.LENGTH_SHORT)
                                            .setAction("", null).show();
//                                    items.getResult().get(position).setIsLiked("Y");
                                }
                            });
                            break;
                        case R.id.menu_unlike:
                            new ApisHelper().likeCommentApi(context, items.getResult().get(position).getCommentId(), "n", new ApisHelper.LikeCommentCallback() {

                                @Override
                                public void onLikeCommentSuccess(String msg) {
                                    Snackbar.make(((Activity) context).getWindow().getDecorView(), msg, Snackbar.LENGTH_SHORT)
                                            .setAction("", null).show();
                                    items.getResult().get(position).setIsLiked("N");
                                    items.getResult().get(position).setCommentLikeCount(String.valueOf((Integer.parseInt(items.getResult().get(position).getCommentLikeCount()) - 1)));
                                    notifyDataSetChanged();
                                }

                                @Override
                                public void onLikeCommentFailure() {
                                    Snackbar.make(((Activity) context).getWindow().getDecorView(), "Something went's wrong", Snackbar.LENGTH_SHORT)
                                            .setAction("", null).show();
                                }
                            });
                            break;
                        case R.id.menu_edit_comment:
                            EditCommentDialog.create(context, items.getResult().get(position).getCommentId(), items.getResult().get(position).getComment(), () -> {
                                callbacks.onEditComment();
                            }).show();
                            break;
                        case R.id.menu_delete_comment:
                            S_Dialogs.getRemoveCommentDialog(context, (dialog, which) -> {
                                new ApisHelper().deleteCommentApi(context, items.getResult().get(position).getCommentId(), new ApisHelper.DeleteCommentCallback() {
                                    @Override
                                    public void onCommentdeletionSuccess(String msg) {
                                        callbacks.onDeleteComment(position);
                                    }

                                    @Override
                                    public void onCommentdeletionFailure() {
                                    }
                                });
                            }).show();
                            break;
                        case R.id.menu_Report_comment:
                            S_Dialogs.getReportCommentDialog(context, (dialog, input) -> {
                                if (TextUtils.isEmpty(input)) {
                                    dialog.dismiss();
                                } else {
                                    new ApisHelper().reportCommentApi(context, input.toString(), items.getResult().get(position).getUserId()
                                            , items.getResult().get(position).getCommentId(), (msg) -> {
                                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }, () -> {
                                                Toast.makeText(context, "Please try again latter.", Toast.LENGTH_SHORT).show();
                                            });
                                }
                            }).show();
                            break;
                        case R.id.menu_cancel_comment:
                            popup.dismiss();
                            break;

                    }

                } else {
                    S_Dialogs.getLoginDialog(context).show();
                }

                return false;
            }
        });
    }

    public interface CommentCallbacks {
        void onEditComment();

        void onDeleteComment(int pos);
    }
}
