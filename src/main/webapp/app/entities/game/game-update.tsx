import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label, UncontrolledTooltip } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IGamer } from 'app/shared/model/gamer.model';
import { getEntities as getGamers } from 'app/entities/gamer/gamer.reducer';
import { getEntity, updateEntity, createEntity, reset } from './game.reducer';
import { IGame } from 'app/shared/model/game.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IGameUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const GameUpdate = (props: IGameUpdateProps) => {
  const [idsgamer, setIdsgamer] = useState([]);
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { gameEntity, gamers, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/game');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getGamers();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...gameEntity,
        ...values,
        gamers: mapIdList(values.gamers),
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="gamoLifeApp.game.home.createOrEditLabel" data-cy="GameCreateUpdateHeading">
            <Translate contentKey="gamoLifeApp.game.home.createOrEditLabel">Create or edit a Game</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : gameEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="game-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="game-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="nameLabel" for="game-name">
                  <Translate contentKey="gamoLifeApp.game.name">Name</Translate>
                </Label>
                <AvField id="game-name" data-cy="name" type="text" name="name" />
                <UncontrolledTooltip target="nameLabel">
                  <Translate contentKey="gamoLifeApp.game.help.name" />
                </UncontrolledTooltip>
              </AvGroup>
              <AvGroup>
                <Label for="game-gamer">
                  <Translate contentKey="gamoLifeApp.game.gamer">Gamer</Translate>
                </Label>
                <AvInput
                  id="game-gamer"
                  data-cy="gamer"
                  type="select"
                  multiple
                  className="form-control"
                  name="gamers"
                  value={!isNew && gameEntity.gamers && gameEntity.gamers.map(e => e.id)}
                >
                  <option value="" key="0" />
                  {gamers
                    ? gamers.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/game" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  gamers: storeState.gamer.entities,
  gameEntity: storeState.game.entity,
  loading: storeState.game.loading,
  updating: storeState.game.updating,
  updateSuccess: storeState.game.updateSuccess,
});

const mapDispatchToProps = {
  getGamers,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(GameUpdate);
