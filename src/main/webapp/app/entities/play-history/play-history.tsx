import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './play-history.reducer';
import { IPlayHistory } from 'app/shared/model/play-history.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPlayHistoryProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const PlayHistory = (props: IPlayHistoryProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const handleSyncList = () => {
    props.getEntities();
  };

  const { playHistoryList, match, loading } = props;
  return (
    <div>
      <h2 id="play-history-heading" data-cy="PlayHistoryHeading">
        <Translate contentKey="gamoLifeApp.playHistory.home.title">Play Histories</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="gamoLifeApp.playHistory.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="gamoLifeApp.playHistory.home.createLabel">Create new Play History</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {playHistoryList && playHistoryList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="gamoLifeApp.playHistory.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="gamoLifeApp.playHistory.maxPlay">Max Play</Translate>
                </th>
                <th>
                  <Translate contentKey="gamoLifeApp.playHistory.datePlays">Date Plays</Translate>
                </th>
                <th>
                  <Translate contentKey="gamoLifeApp.playHistory.playDate">Play Date</Translate>
                </th>
                <th>
                  <Translate contentKey="gamoLifeApp.playHistory.gamer">Gamer</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {playHistoryList.map((playHistory, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${playHistory.id}`} color="link" size="sm">
                      {playHistory.id}
                    </Button>
                  </td>
                  <td>{playHistory.maxPlay}</td>
                  <td>{playHistory.datePlays}</td>
                  <td>
                    {playHistory.playDate ? <TextFormat type="date" value={playHistory.playDate} format={APP_LOCAL_DATE_FORMAT} /> : null}
                  </td>
                  <td>{playHistory.gamer ? <Link to={`gamer/${playHistory.gamer.id}`}>{playHistory.gamer.id}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${playHistory.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${playHistory.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${playHistory.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="gamoLifeApp.playHistory.home.notFound">No Play Histories found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ playHistory }: IRootState) => ({
  playHistoryList: playHistory.entities,
  loading: playHistory.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PlayHistory);
