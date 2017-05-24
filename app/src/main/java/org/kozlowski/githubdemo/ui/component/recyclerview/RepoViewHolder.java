package org.kozlowski.githubdemo.ui.component.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.kozlowski.githubdemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;

/**
 * Created by and on 16.05.17.
 */

public class RepoViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tvRepoName)
    protected TextView tvRepoName;
    @BindView(R.id.tvRepoDescription)
    protected TextView tvDescription;
    @BindView(R.id.tvRepoLoginOwner)
    protected TextView tvLoginOwner;

    private View itemView;

    public RepoViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.itemView = itemView;
    }

    public void render(final RepoItem repoItem, final ObservableEmitter<String> itemLongClickEmitter){
        tvRepoName.setText(repoItem.getRepoName());
        tvDescription.setText(repoItem.getDescription());
        tvLoginOwner.setText(repoItem.getLoginOwner());
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                itemLongClickEmitter.onNext(repoItem.getHtmlUrl());
                return false;
            }
        });
    }
}
