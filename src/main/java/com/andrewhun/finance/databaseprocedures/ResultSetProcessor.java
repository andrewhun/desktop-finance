/*
* This file contains the ResultSetProcessor class, which
* encapsulates the logic of creating single model objects
* and lists of model objects from a ResultSet.
 */

package com.andrewhun.finance.databaseprocedures;

import java.util.List;
import java.sql.ResultSet;
import java.util.ArrayList;
import com.andrewhun.finance.exceptions.*;

abstract class ResultSetProcessor<T> extends StoredProceduresBaseClass {

    T getSingleEntry(ResultSet resultSet) throws Exception {

        T model;
        if (resultSet.next()) {

            model = createModelFromResultSet(resultSet);
            if (resultSet.next()) {
                throw(new MultipleEntriesFoundException());
            }
        }
        else {

            throw(new EntryNotFoundException());
        }

        return model;
    }

    List<T> getListOfEntries(ResultSet resultSet) throws Exception {

        List<T> models = new ArrayList<>();

        while (resultSet.next()) {

            models.add(createModelFromResultSet(resultSet));
        }
        return models;
    }

    abstract T createModelFromResultSet(ResultSet resultSet) throws Exception;
}