package services.impl;


import dao.TableDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.Table;
import model.errors.Error;
import services.TablesService;

import java.util.List;

public class TablesServiceImpl implements TablesService {

    private final TableDAO dao;

    @Inject
    public TablesServiceImpl(TableDAO dao) {
        this.dao = dao;
    }

    @Override
    public Either<Error, List<Table>> getAll() {
        return dao.getAll();
    }
}