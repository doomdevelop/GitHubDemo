package org.kozlowski.githubdemo.domain.usecase;

import org.kozlowski.githubdemo.data.model.User;
import org.kozlowski.githubdemo.repository.DataRepository;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.annotations.Nullable;

/**
 * Created by and on 26.05.17.
 */

public class ReadUserDataUseCase extends UseCase<User, Void> {
    private final DataRepository dataRepository;

    @Inject
    public ReadUserDataUseCase(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Override
    protected Observable<User> buildUseCaseObservable(Void unused) {
        return dataRepository.readUser();
    }
}
