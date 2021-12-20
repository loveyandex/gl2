import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './gamer.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IGamerDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const GamerDetail = (props: IGamerDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { gamerEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="gamerDetailsHeading">
          <Translate contentKey="gamoLifeApp.gamer.detail.title">Gamer</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{gamerEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="gamoLifeApp.gamer.name">Name</Translate>
            </span>
          </dt>
          <dd>{gamerEntity.name}</dd>
          <dt>
            <span id="phonenumber">
              <Translate contentKey="gamoLifeApp.gamer.phonenumber">Phonenumber</Translate>
            </span>
          </dt>
          <dd>{gamerEntity.phonenumber}</dd>
          <dt>
            <span id="verifyCode">
              <Translate contentKey="gamoLifeApp.gamer.verifyCode">Verify Code</Translate>
            </span>
          </dt>
          <dd>{gamerEntity.verifyCode}</dd>
          <dt>
            <span id="referalCode">
              <Translate contentKey="gamoLifeApp.gamer.referalCode">Referal Code</Translate>
            </span>
          </dt>
          <dd>{gamerEntity.referalCode}</dd>
          <dt>
            <span id="score">
              <Translate contentKey="gamoLifeApp.gamer.score">Score</Translate>
            </span>
          </dt>
          <dd>{gamerEntity.score}</dd>
          <dt>
            <span id="canplayGameToday">
              <Translate contentKey="gamoLifeApp.gamer.canplayGameToday">Canplay Game Today</Translate>
            </span>
          </dt>
          <dd>{gamerEntity.canplayGameToday ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="gamoLifeApp.gamer.user">User</Translate>
          </dt>
          <dd>{gamerEntity.user ? gamerEntity.user.id : ''}</dd>
          <dt>
            <Translate contentKey="gamoLifeApp.gamer.inviter">Inviter</Translate>
          </dt>
          <dd>{gamerEntity.inviter ? gamerEntity.inviter.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/gamer" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/gamer/${gamerEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ gamer }: IRootState) => ({
  gamerEntity: gamer.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(GamerDetail);
