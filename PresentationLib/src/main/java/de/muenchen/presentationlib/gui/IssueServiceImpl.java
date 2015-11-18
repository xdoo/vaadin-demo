package de.muenchen.presentationlib.gui;

import de.muenchen.presentationlib.api.Issue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by maximilian.zollbrecht on 18.11.15.
 */
public class IssueServiceImpl implements IssueService {

    private final Logger LOG = LoggerFactory.getLogger(IssueServiceImpl.class);

    @Override
    public void createIssue(Issue issue){
        LOG.info("trying to create Issue");
    }
}
