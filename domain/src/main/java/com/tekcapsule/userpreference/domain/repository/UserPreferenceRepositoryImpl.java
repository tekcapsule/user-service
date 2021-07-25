package com.tekcapsule.userpreference.domain.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.tekcapsule.userpreference.domain.model.Mentor;
import com.tekcapsule.userpreference.domain.query.SearchItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class UserPreferenceRepositoryImpl implements UserPreferenceDynamoRepository {

    private DynamoDBMapper dynamo;

    @Autowired
    public UserPreferenceRepositoryImpl(DynamoDBMapper dynamo) {
        this.dynamo = dynamo;
    }

    @Override
    public List<Mentor> findAll(String tenantId) {

        Mentor hashKey = Mentor.builder().tenantId(tenantId).build();
        DynamoDBQueryExpression<Mentor> queryExpression = new DynamoDBQueryExpression<Mentor>()
                .withHashKeyValues(hashKey);

        return dynamo.query(Mentor.class, queryExpression);
    }

    @Override
    public Mentor findBy(String tenantId, String userId) {
        return dynamo.load(Mentor.class, tenantId, userId);
    }

    @Override
    public Mentor save(Mentor mentor) {
        dynamo.save(mentor);
        return mentor;
    }

    @Override
    public void delete(String tenantId, String id) {
        Mentor mentor = findBy(tenantId, id);
        if (mentor != null) {
            dynamo.delete(mentor);
        }
    }

    @Override
    public void disableById(String tenantId, String id) {
        Mentor mentor = findBy(tenantId, id);
        if (mentor != null) {
            mentor.setActive(false);
            dynamo.save(mentor);
        }
    }

    @Override
    public List<SearchItem> search(String tenantId) {
        Mentor hashKey = Mentor.builder().tenantId(tenantId).build();
        DynamoDBQueryExpression<Mentor> queryExpression = new DynamoDBQueryExpression<Mentor>()
                .withHashKeyValues(hashKey);
        List<Mentor> mentors = dynamo.query(Mentor.class, queryExpression);
        List<SearchItem> searchItems = new ArrayList<SearchItem>();
        if (mentors != null) {
            searchItems = mentors.stream().map(mentor -> {
                return SearchItem.builder()
                        .activeSince(mentor.getActiveSince())
                        .headLine(mentor.getHeadLine())
                        .name(mentor.getName())
                        .photoUrl(mentor.getPhotoUrl())
                        .rating(mentor.getRating())
                        .social(mentor.getSocial())
                        .build();
            }).collect(Collectors.toList());
        }
        return searchItems;
    }
}
