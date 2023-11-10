package dao;

import io.vavr.control.Either;
import model.Table;
import model.errors.Error;

import java.util.List;

public interface TableDAO {
    Either<Error, List<Table>> getAll();
}
