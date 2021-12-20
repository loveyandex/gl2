import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './gamer.reducer';
import { IGamer } from 'app/shared/model/gamer.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IGamerProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const Gamer = (props: IGamerProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const handleSyncList = () => {
    props.getEntities();
  };

  const { gamerList, match, loading } = props;
  return (
    <div>
      <h2 id="gamer-heading" data-cy="GamerHeading">
        <Translate contentKey="gamoLifeApp.gamer.home.title">Gamers</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="gamoLifeApp.gamer.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="gamoLifeApp.gamer.home.createLabel">Create new Gamer</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {gamerList && gamerList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="gamoLifeApp.gamer.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="gamoLifeApp.gamer.name">Name</Translate>
                </th>
                <th>
                  <Translate contentKey="gamoLifeApp.gamer.phonenumber">Phonenumber</Translate>
                </th>
                <th>
                  <Translate contentKey="gamoLifeApp.gamer.verifyCode">Verify Code</Translate>
                </th>
                <th>
                  <Translate contentKey="gamoLifeApp.gamer.referalCode">Referal Code</Translate>
                </th>
                <th>
                  <Translate contentKey="gamoLifeApp.gamer.score">Score</Translate>
                </th>
                <th>
                  <Translate contentKey="gamoLifeApp.gamer.canplayGameToday">Canplay Game Today</Translate>
                </th>
                <th>
                  <Translate contentKey="gamoLifeApp.gamer.user">User</Translate>
                </th>
                <th>
                  <Translate contentKey="gamoLifeApp.gamer.inviter">Inviter</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {gamerList.map((gamer, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${gamer.id}`} color="link" size="sm">
                      {gamer.id}
                    </Button>
                  </td>
                  <td>{gamer.name}</td>
                  <td>{gamer.phonenumber}</td>
                  <td>{gamer.verifyCode}</td>
                  <td>{gamer.referalCode}</td>
                  <td>{gamer.score}</td>
                  <td>{gamer.canplayGameToday ? 'true' : 'false'}</td>
                  <td>{gamer.user ? gamer.user.id : ''}</td>
                  <td>{gamer.inviter ? <Link to={`gamer/${gamer.inviter.id}`}>{gamer.inviter.id}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${gamer.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${gamer.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${gamer.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="gamoLifeApp.gamer.home.notFound">No Gamers found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ gamer }: IRootState) => ({
  gamerList: gamer.entities,
  loading: gamer.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(Gamer);
