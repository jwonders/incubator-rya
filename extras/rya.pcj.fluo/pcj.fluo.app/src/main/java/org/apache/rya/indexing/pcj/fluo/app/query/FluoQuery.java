/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.rya.indexing.pcj.fluo.app.query;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.builder.EqualsBuilder;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;

import edu.umd.cs.findbugs.annotations.DefaultAnnotation;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import net.jcip.annotations.Immutable;

/**
 * Metadata for every node of a query that is being updated by the Fluo application.
 */
@Immutable
@DefaultAnnotation(NonNull.class)
public class FluoQuery {

    private final QueryMetadata queryMetadata;
    private final ImmutableMap<String, StatementPatternMetadata> statementPatternMetadata;
    private final ImmutableMap<String, FilterMetadata> filterMetadata;
    private final ImmutableMap<String, JoinMetadata> joinMetadata;
    private final ImmutableMap<String, AggregationMetadata> aggregationMetadata;

    /**
     * Constructs an instance of {@link FluoQuery}. Private because applications
     * must use {@link Builder} instead.
     *
     * @param queryMetadata - The root node of a query that is updated in Fluo. (not null)
     * @param statementPatternMetadata - A map from Node ID to Statement Pattern metadata as
     *   it is represented within the Fluo app. (not null)
     * @param filterMetadata - A map from Node ID to Filter metadata as it is represented
     *   within the Fluo app. (not null)
     * @param joinMetadata - A map from Node ID to Join metadata as it is represented
     *   within the Fluo app. (not null)
     * @param aggregationMetadata - A map from Node ID to Aggregation metadata as it is
     *   represented within the Fluo app. (not null)
     */
    private FluoQuery(
            final QueryMetadata queryMetadata,
            final ImmutableMap<String, StatementPatternMetadata> statementPatternMetadata,
            final ImmutableMap<String, FilterMetadata> filterMetadata,
            final ImmutableMap<String, JoinMetadata> joinMetadata,
            final ImmutableMap<String, AggregationMetadata> aggregationMetadata) {
        this.queryMetadata = requireNonNull(queryMetadata);
        this.statementPatternMetadata = requireNonNull(statementPatternMetadata);
        this.filterMetadata = requireNonNull(filterMetadata);
        this.joinMetadata = requireNonNull(joinMetadata);
        this.aggregationMetadata = requireNonNull(aggregationMetadata);
    }

    /**
     * @return Metadata about the root node of a query that is updated within the Fluo app.
     */
    public QueryMetadata getQueryMetadata() {
        return queryMetadata;
    }

    /**
     * Get a Statement Pattern node's metadata.
     *
     * @param nodeId - The node ID of the StatementPattern metadata you want. (not null)
     * @return The StatementPattern metadata if it could be found; otherwise absent.
     */
    public Optional<StatementPatternMetadata> getStatementPatternMetadata(final String nodeId) {
        requireNonNull(nodeId);
        return Optional.fromNullable( statementPatternMetadata.get(nodeId) );
    }

    /**
     * @return All of the Statement Pattern metadata that is stored for the query.
     */
    public Collection<StatementPatternMetadata> getStatementPatternMetadata() {
        return statementPatternMetadata.values();
    }

    /**
     * Get a Filter node's metadata.
     *
     * @param nodeId - The node ID of the Filter metadata you want. (not null)
     * @return The Filter metadata if it could be found; otherwise absent.
     */
    public Optional<FilterMetadata> getFilterMetadata(final String nodeId) {
        requireNonNull(nodeId);
        return Optional.fromNullable( filterMetadata.get(nodeId) );
    }

    /**
     * @return All of the Filter metadata that is stored for the query.
     */
    public Collection<FilterMetadata> getFilterMetadata() {
        return filterMetadata.values();
    }

    /**
     * Get a Join node's metadata.
     *
     * @param nodeId - The node ID of the Join metadata you want. (not null)
     * @return The Join metadata if it could be found; otherwise absent.
     */
    public Optional<JoinMetadata> getJoinMetadata(final String nodeId) {
        requireNonNull(nodeId);
        return Optional.fromNullable( joinMetadata.get(nodeId) );
    }

    /**
     * @return All of the Join metadata that is stored for the query.
     */
    public Collection<JoinMetadata> getJoinMetadata() {
        return joinMetadata.values();
    }

    /**
     * Get an Aggregation node's metadata.
     *
     * @param nodeId - The node ID of the Aggregation metadata you want. (not null)
     * @return The Aggregation metadata if it could be found; otherwise absent.
     */
    public Optional<AggregationMetadata> getAggregationMetadata(final String nodeId) {
        requireNonNull(nodeId);
        return Optional.fromNullable( aggregationMetadata.get(nodeId) );
    }

    /**
     * @return All of the Aggregation metadata that is stored for the query.
     */
    public Collection<AggregationMetadata> getAggregationMetadata() {
        return aggregationMetadata.values();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(
                queryMetadata,
                statementPatternMetadata,
                filterMetadata,
                joinMetadata,
                aggregationMetadata);
    }

    @Override
    public boolean equals(final Object o) {
        if(this == o) {
            return true;
        }

        if(o instanceof FluoQuery) {
            final FluoQuery fluoQuery = (FluoQuery)o;
            return new EqualsBuilder()
                    .append(queryMetadata, fluoQuery.queryMetadata)
                    .append(statementPatternMetadata, fluoQuery.statementPatternMetadata)
                    .append(filterMetadata, fluoQuery.filterMetadata)
                    .append(joinMetadata, fluoQuery.joinMetadata)
                    .append(aggregationMetadata, fluoQuery.aggregationMetadata)
                    .isEquals();
        }

        return false;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();

        if(queryMetadata != null) {
            builder.append( queryMetadata.toString() );
            builder.append("\n");
        }

        for(final FilterMetadata metadata : filterMetadata.values()) {
            builder.append(metadata);
            builder.append("\n");
        }

        for(final JoinMetadata metadata : joinMetadata.values()) {
            builder.append(metadata.toString());
            builder.append("\n");
        }

        for(final StatementPatternMetadata metadata : statementPatternMetadata.values()) {
            builder.append(metadata.toString());
            builder.append("\n");
        }

        for(final AggregationMetadata metadata : aggregationMetadata.values()) {
            builder.append(metadata.toString());
            builder.append("\n");
        }

        return builder.toString();
    }

    /**
     * @return A new {@link Builder} for this class.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builds instances of {@link FluoQuery}.
     */
    @DefaultAnnotation(NonNull.class)
    public static final class Builder {

        private QueryMetadata.Builder queryBuilder = null;
        private final Map<String, StatementPatternMetadata.Builder> spBuilders = new HashMap<>();
        private final Map<String, FilterMetadata.Builder> filterBuilders = new HashMap<>();
        private final Map<String, JoinMetadata.Builder> joinBuilders = new HashMap<>();
        private final Map<String, AggregationMetadata.Builder> aggregationBuilders = new HashMap<>();

        /**
         * Sets the {@link QueryMetadata.Builder} that is used by this builder.
         *
         * @param queryMetadata - The builder representing the query's results.
         * @return This builder so that method invocation may be chained.
         */
        public Builder setQueryMetadata(@Nullable final QueryMetadata.Builder queryMetadata) {
            this.queryBuilder = queryMetadata;
            return this;
        }

        /**
         * @return The Query metadata builder if one has been set.
         */
        public Optional<QueryMetadata.Builder> getQueryBuilder() {
            return Optional.fromNullable( queryBuilder );
        }

        /**
         * Adds a new {@link StatementPatternMetadata.Builder} to this builder.
         *
         * @param spBuilder - A builder representing a specific Statement Pattern within the query. (not null)
         * @return This builder so that method invocation may be chained.
         */
        public Builder addStatementPatternBuilder(final StatementPatternMetadata.Builder spBuilder) {
            requireNonNull(spBuilder);
            spBuilders.put(spBuilder.getNodeId(), spBuilder);
            return this;
        }

        /**
         * Get a Statement Pattern builder from this builder.
         *
         * @param nodeId - The Node ID the Statement Pattern builder was stored at. (not null)
         * @return The builder that was stored at the node id if one was found.
         */
        public Optional<StatementPatternMetadata.Builder> getStatementPatternBuilder(final String nodeId) {
            requireNonNull(nodeId);
            return Optional.fromNullable( spBuilders.get(nodeId) );
        }

        /**
         * Adds a new {@link FilterMetadata.Builder} to this builder.
         *
         * @param filterBuilder - A builder representing a specific Filter within the query. (not null)
         * @return This builder so that method invocation may be chained.
         */
        public Builder addFilterMetadata(final FilterMetadata.Builder filterBuilder) {
            requireNonNull(filterBuilder);
            this.filterBuilders.put(filterBuilder.getNodeId(), filterBuilder);
            return this;
        }

        /**
         * Get a Filter builder from this builder.
         *
         * @param nodeId - The Node ID the Filter builder was stored at. (not null)
         * @return The builder that was stored at the node id if one was found.
         */
        public Optional<FilterMetadata.Builder> getFilterBuilder(final String nodeId) {
            requireNonNull(nodeId);
            return Optional.fromNullable( filterBuilders.get(nodeId) );
        }

        /**
         * Adds a new {@link JoinMetadata.Builder} to this builder.
         *
         * @param joinBuilder - A builder representing a specific Join within the query. (not null)
         * @return This builder so that method invocation may be chained.
         */
        public Builder addJoinMetadata(final JoinMetadata.Builder joinBuilder) {
            requireNonNull(joinBuilder);
            this.joinBuilders.put(joinBuilder.getNodeId(), joinBuilder);
            return this;
        }

        /**
         * Get an Aggregate builder from this builder.
         *
         * @param nodeId - The Node ID the Aggregate builder was stored at. (not null)
         * @return The builder that was stored at the node id if one was found.
         */
        public Optional<AggregationMetadata.Builder> getAggregateBuilder(final String nodeId) {
            requireNonNull(nodeId);
            return Optional.fromNullable( aggregationBuilders.get(nodeId) );
        }

        /**
         * Adds a new {@link AggregationMetadata.Builder} to this builder.
         *
         * @param aggregationBuilder - A builder representing a specific Aggregation within the Query. (not null)
         * @return This builder so that method invocation may be chained.
         */
        public Builder addAggregateMetadata(@Nullable final AggregationMetadata.Builder aggregationBuilder) {
            requireNonNull(aggregationBuilder);
            this.aggregationBuilders.put(aggregationBuilder.getNodeId(), aggregationBuilder);
            return this;
        }

        /**
         * Get a Join builder from this builder.
         *
         * @param nodeId - The Node ID the Join builder was stored at. (not null)
         * @return The builder that was stored at the node id if one was found.
         */
        public Optional<JoinMetadata.Builder> getJoinBuilder(final String nodeId) {
            requireNonNull(nodeId);
            return Optional.fromNullable( joinBuilders.get(nodeId) );
        }

        /**
         * @return Creates a {@link FluoQuery} using the values that have been supplied to this builder.
         */
        public FluoQuery build() {
            final QueryMetadata queryMetadata = queryBuilder.build();

            final ImmutableMap.Builder<String, StatementPatternMetadata> spMetadata = ImmutableMap.builder();
            for(final Entry<String, StatementPatternMetadata.Builder> entry : spBuilders.entrySet()) {
                spMetadata.put(entry.getKey(), entry.getValue().build());
            }

            final ImmutableMap.Builder<String, FilterMetadata> filterMetadata = ImmutableMap.builder();
            for(final Entry<String, FilterMetadata.Builder> entry : filterBuilders.entrySet()) {
                filterMetadata.put(entry.getKey(), entry.getValue().build());
            }

            final ImmutableMap.Builder<String, JoinMetadata> joinMetadata = ImmutableMap.builder();
            for(final Entry<String, JoinMetadata.Builder> entry : joinBuilders.entrySet()) {
                joinMetadata.put(entry.getKey(), entry.getValue().build());
            }

            final ImmutableMap.Builder<String, AggregationMetadata> aggregateMetadata = ImmutableMap.builder();
            for(final Entry<String, AggregationMetadata.Builder> entry : aggregationBuilders.entrySet()) {
                aggregateMetadata.put(entry.getKey(), entry.getValue().build());
            }

            return new FluoQuery(queryMetadata, spMetadata.build(), filterMetadata.build(), joinMetadata.build(), aggregateMetadata.build());
        }
    }
}