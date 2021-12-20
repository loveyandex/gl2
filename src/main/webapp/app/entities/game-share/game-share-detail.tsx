import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, UncontrolledTooltip, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './game-share.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IGameShareDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const GameShareDetail = (props: IGameShareDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { gameShareEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="gameShareDetailsHeading">
          <Translate contentKey="gamoLifeApp.gameShare.detail.title">GameShare</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{gameShareEntity.id}</dd>
          <dt>
            <span id="maxPlay">
              <Translate contentKey="gamoLifeApp.gameShare.maxPlay">Max Play</Translate>
            </span>
            <UncontrolledTooltip target="maxPlay">
              <Translate contentKey="gamoLifeApp.gameShare.help.maxPlay" />
            </UncontrolledTooltip>
          </dt>
          <dd>{gameShareEntity.maxPlay}</dd>
          <dt>
            <span id="shareTime">
              <Translate contentKey="gamoLifeApp.gameShare.shareTime">Share Time</Translate>
            </span>
          </dt>
          <dd>
            {gameShareEntity.shareTime ? <TextFormat value={gameShareEntity.shareTime} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="gamoLifeApp.gameShare.gamer">Gamer</Translate>
          </dt>
          <dd>{gameShareEntity.gamer ? gameShareEntity.gamer.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/game-share" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/game-share/${gameShareEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ gameShare }: IRootState) => ({
  gameShareEntity: gameShare.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(GameShareDetail);
