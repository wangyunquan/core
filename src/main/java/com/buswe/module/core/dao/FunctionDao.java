package com.buswe.module.core.dao;

import com.buswe.core.dao.jpa.BaseRepository;
import com.buswe.module.core.entity.Function;

public interface FunctionDao extends BaseRepository<Function, String> {
    public boolean existsByLevelCode (String levelCode);
}
