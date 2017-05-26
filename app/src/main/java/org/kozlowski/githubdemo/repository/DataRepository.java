package org.kozlowski.githubdemo.repository;

import org.kozlowski.githubdemo.data.DaTaRepoMapper;
import org.kozlowski.githubdemo.data.model.RepoData;
import org.kozlowski.githubdemo.data.model.User;
import org.kozlowski.githubdemo.ui.component.recyclerview.RepoItem;
import org.kozlowski.githubdemo.util.Constants;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by and on 16.05.17.
 */
@Singleton
public class DataRepository {
    private final ApiRepository apiRepository;
    private final LocalRepository localRepository;

    @Inject
    public DataRepository(ApiRepository apiRepository,LocalRepository localRepository) {
        this.apiRepository = apiRepository;
        this.localRepository = localRepository;
    }

    public Observable<List<RepoItem>> listRepositories(String user, String accessToken){

        return apiRepository.listRepositories(user,accessToken).map(generateTransformFunction());
    }

    public Observable<List<RepoItem>> listRepositories(String user, String accessToken,int page){

        return apiRepository.listRepositories(user,accessToken,page, Constants.PER_PAGE).
            map(generateTransformFunction());
    }

    private Function<List<RepoData>, List<RepoItem>> generateTransformFunction() {
        return new Function<List<RepoData>,List<RepoItem>>() {
            @Override
            public List<RepoItem> apply(@NonNull List<RepoData> repoDatas) throws Exception {
                return DaTaRepoMapper.transform(repoDatas);
            }
        };
    }

    public Observable<Boolean> writeUser(User user){
        return localRepository.writeUser(user);
    }

    public Observable<User> readUser(){
        return localRepository.readUser();
    }
}
