package org.kozlowski.githubdemo.repository;

import org.kozlowski.githubdemo.MyApplication;
import org.kozlowski.githubdemo.R;
import org.kozlowski.githubdemo.data.model.RepoData;
import org.kozlowski.githubdemo.data.remote.service.RepoService;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.http.Query;

/**
 * Created by and on 16.05.17.
 */
@Singleton
public class ApiRepository {

    private final Retrofit retrofit;

    @Inject
    public ApiRepository(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    public Observable<List<RepoData>> listRepositories(String user, String accessToken){
       RepoService repoService = retrofit.create(RepoService.class);
        return repoService.listRepos(user);
    }
    public Observable<List<RepoData>> listRepositories(String user, String accessToken,int page,
                                                       int perPage){
        RepoService repoService = retrofit.create(RepoService.class);
        return repoService.listRepos(user,accessToken,String.valueOf(page),String.valueOf(perPage));
    }

}
