package org.kozlowski.githubdemo.ui.component.recyclerview;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.kozlowski.githubdemo.util.Constants;

import io.reactivex.FlowableEmitter;

/**
 * Created by and on 16.05.17.
 */
public class RepoScrollListener extends RecyclerView.OnScrollListener {
    private int nextOffset;
    private int initialLimit = Constants.PER_PAGE;
    private int page = 2;
    private final FlowableEmitter emitter;
    private static final String TAG = RepoScrollListener.class.getSimpleName();

    public RepoScrollListener(FlowableEmitter emitter) {
        this.emitter = emitter;
    }

    public RepoScrollListener(FlowableEmitter emitter, int initialLimit) {
        this.emitter = emitter;
        this.initialLimit = initialLimit;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        final int visibleItemCount = layoutManager.getChildCount();
        final int totalItemCount = layoutManager.getItemCount();
        final int lastVisibleItemPos = layoutManager.findLastVisibleItemPosition();
        final int limit = initialLimit;

        // values
        final int offset = totalItemCount;
        final int updateBoarderForLastItemPos = (totalItemCount / 2);
        final boolean isScrollingDown = 0 < dy;

        // no updates, already triggered new data for that offset or already work in progress
        final boolean stillWorking = offset < nextOffset;
        if (stillWorking) {
            return;
        }

        // load new items if user scrolled to half list and there are more to show
        if (isScrollingDown && lastVisibleItemPos >= updateBoarderForLastItemPos
            && totalItemCount >= initialLimit) {
            // Show loading state to user
//            if (recyclerView.getAdapter() instanceof RecyclerViewPaginationAdapter) {
//                ((RepoAdapter) recyclerView.getAdapter()).;
        }

        emitter.onNext(page);
        page++;
        nextOffset = offset + limit;
        Log.i(TAG, "onNext {offset=" + offset + ", limit=" + limit + ", nextOffset=" +
            nextOffset + "}");
    }
}
