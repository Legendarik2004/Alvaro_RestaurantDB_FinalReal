package dao;

import io.vavr.control.Either;
import model.MenuItem;
import model.errors.Error;

import java.util.List;

public interface MenuItemsDAO {
    Either<Error, List<MenuItem>> getAll();
}
