package org.kozlowski.githubdemo.domain.usecase;

import org.kozlowski.githubdemo.repository.DataRepository;
import org.kozlowski.githubdemo.ui.component.recyclerview.RepoItem;

import java.util.List;

import javax.inject.Inject;


import io.reactivex.Observable;

/**
 * Created by and on 17.05.17.
 */

public class GetRepositoriesListUseCase extends UseCase<List<RepoItem>,GetRepositoriesListUseCase
    .Params> {

    private final DataRepository dataRepository;

    @Inject
    public GetRepositoriesListUseCase(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Override
    protected Observable<List<RepoItem>> buildUseCaseObservable(Params params) {
        return dataRepository.listRepositories(params.userName, params.userToken,params.page);
    }

    public static final class Params {

        private final String userName;
        private final String userToken;
        private final int page;

        public Params( String userName, String userToken,int page) {
            this.userName = userName;
            this.userToken = userToken;
            this.page = page;
        }

        public static Params create(String userName, String userToken,int page) {
            return new Params(userName, userToken,page);
        }
    }
}
