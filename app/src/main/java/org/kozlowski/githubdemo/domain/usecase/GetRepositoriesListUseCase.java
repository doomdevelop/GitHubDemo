package org.kozlowski.githubdemo.domain.usecase;

import android.provider.SyncStateContract;

import org.kozlowski.githubdemo.repository.ApiRepository;
import org.kozlowski.githubdemo.repository.DataRepository;
import org.kozlowski.githubdemo.util.Constants;

import javax.inject.Inject;


import io.reactivex.Observable;

import static org.kozlowski.githubdemo.util.Constants.ACCESS_TOKEN;

/**
 * Created by and on 17.05.17.
 */

public class GetRepositoriesListUseCase extends UseCase {

    private final DataRepository dataRepository;
    private int page;

    public synchronized void setPage(int page) {
        this.page = page;
    }

    private synchronized int getPage(){
        return page;
    }

    @Inject
    public GetRepositoriesListUseCase(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return dataRepository.listRepositories(Constants.USER, ACCESS_TOKEN,getPage());
    }
}
