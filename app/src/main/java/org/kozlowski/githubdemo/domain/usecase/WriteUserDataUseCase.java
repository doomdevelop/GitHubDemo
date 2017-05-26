package org.kozlowski.githubdemo.domain.usecase;

import org.kozlowski.githubdemo.data.model.User;
import org.kozlowski.githubdemo.repository.DataRepository;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by and on 26.05.17.
 */

public class WriteUserDataUseCase extends UseCase<Boolean,WriteUserDataUseCase.Params> {
    private final DataRepository dataRepository;
    @Inject
    public WriteUserDataUseCase(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Override
    protected Observable<Boolean> buildUseCaseObservable(Params params) {
        return dataRepository.writeUser(params.user);
    }

    public static final class Params {

        private final User user;

        public Params(User user) {
            this.user = user;
        }


        public static WriteUserDataUseCase.Params create(User user) {
            return new WriteUserDataUseCase.Params(user);
        }
    }
}
