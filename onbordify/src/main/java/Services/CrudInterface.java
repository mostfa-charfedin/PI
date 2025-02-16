package Services;

import modles.Quiz;
import modles.Reponse;

import java.util.List;

public interface CrudInterface<T> {
    void create(T obj) throws Exception;

    void update(T obj) throws Exception;

    void delete(int id) throws Exception;

    List<T> getAll() throws Exception;
}
