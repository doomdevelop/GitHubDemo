package org.kozlowski.githubdemo.ui.component.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.kozlowski.githubdemo.R;
import org.kozlowski.githubdemo.data.model.RepoData;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableEmitter;

/**
 * Created by and on 16.05.17.
 */

public class RepoAdapter extends RecyclerView.Adapter<RepoViewHolder> {
    private List<RepoItem> itemList = new ArrayList();
    private static final String TAG = RepoAdapter.class.getSimpleName();
    private ObservableEmitter<String> itemLongClickEmitter;
    @Override
    public RepoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.item_repo, parent, false);
        return new RepoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RepoViewHolder holder, int position) {
        final RepoViewHolder repoViewHolder = holder;
        RepoItem repoItem = itemList.get(position);
        repoViewHolder.render(repoItem,itemLongClickEmitter);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void addItems(List<RepoItem> repos){
        if(itemLongClickEmitter == null){
            throw new RuntimeException("You must setItemLongClickEmitter " +
                "'ObservableEmitter<String>' before data!");
        }
        itemList.addAll(repos);
        notifyDataSetChanged();
        Log.d(TAG,"total item count : "+getItemCount());
    }

    public void setItemLongClickEmitter(ObservableEmitter<String> itemLongClickEmitter){
        this.itemLongClickEmitter = itemLongClickEmitter;
    }
}
