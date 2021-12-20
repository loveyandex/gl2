import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, UncontrolledTooltip, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './game.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IGameDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const GameDetail = (props: IGameDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { gameEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="gameDetailsHeading">
          <Translate contentKey="gamoLifeApp.game.detail.title">Game</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{gameEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="gamoLifeApp.game.name">Name</Translate>
            </span>
            <UncontrolledTooltip target="name">
              <Translate contentKey="gamoLifeApp.game.help.name" />
            </UncontrolledTooltip>
          </dt>
          <dd>{gameEntity.name}</dd>
          <dt>
            <Translate contentKey="gamoLifeApp.game.gamer">Gamer</Translate>
          </dt>
          <dd>
            {gameEntity.gamers
              ? gameEntity.gamers.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {gameEntity.gamers && i === gameEntity.gamers.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/game" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/game/${gameEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ game }: IRootState) => ({
  gameEntity: game.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(GameDetail);
