import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './game-share.reducer';
import { IGameShare } from 'app/shared/model/game-share.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IGameShareProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const GameShare = (props: IGameShareProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const handleSyncList = () => {
    props.getEntities();
  };

  const { gameShareList, match, loading } = props;
  return (
    <div>
      <h2 id="game-share-heading" data-cy="GameShareHeading">
        <Translate contentKey="gamoLifeApp.gameShare.home.title">Game Shares</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="gamoLifeApp.gameShare.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="gamoLifeApp.gameShare.home.createLabel">Create new Game Share</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {gameShareList && gameShareList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="gamoLifeApp.gameShare.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="gamoLifeApp.gameShare.maxPlay">Max Play</Translate>
                </th>
                <th>
                  <Translate contentKey="gamoLifeApp.gameShare.shareTime">Share Time</Translate>
                </th>
                <th>
                  <Translate contentKey="gamoLifeApp.gameShare.gamer">Gamer</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {gameShareList.map((gameShare, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${gameShare.id}`} color="link" size="sm">
                      {gameShare.id}
                    </Button>
                  </td>
                  <td>{gameShare.maxPlay}</td>
                  <td>{gameShare.shareTime ? <TextFormat type="date" value={gameShare.shareTime} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{gameShare.gamer ? <Link to={`gamer/${gameShare.gamer.id}`}>{gameShare.gamer.id}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${gameShare.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${gameShare.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${gameShare.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="gamoLifeApp.gameShare.home.notFound">No Game Shares found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ gameShare }: IRootState) => ({
  gameShareList: gameShare.entities,
  loading: gameShare.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(GameShare);
