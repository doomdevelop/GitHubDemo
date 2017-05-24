package org.kozlowski.githubdemo.data.remote.service;

import org.kozlowski.githubdemo.data.model.RepoData;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by and on 16.05.17.
 */

public interface RepoService {
    @GET("/users/{user}/repos")
    Observable<List<RepoData>> listRepos(@Path("user") String user, @Query("access_token") String
        accessToken,@Query("page") String page,@Query("per_page") String perPage);
    @GET("/users/{user}/repos")
    Observable<List<RepoData>> listRepos(@Path("user") String user, @Query("access_token") String
        accessToken);
    @GET("/users/{user}/repos")
    Observable<List<RepoData>> listRepos(@Path("user") String user);
}
