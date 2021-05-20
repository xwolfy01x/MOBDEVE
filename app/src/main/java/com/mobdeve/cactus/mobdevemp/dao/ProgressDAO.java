package com.mobdeve.cactus.mobdevemp.dao;

import com.mobdeve.cactus.mobdevemp.models.Progress;

public interface ProgressDAO {
    Progress getOneProgress(String username);
    void initializeProgress(Progress oneProgress);
}