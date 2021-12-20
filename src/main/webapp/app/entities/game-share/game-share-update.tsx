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
import { getEntity, updateEntity, createEntity, reset } from './game-share.reducer';
import { IGameShare } from 'app/shared/model/game-share.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IGameShareUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const GameShareUpdate = (props: IGameShareUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { gameShareEntity, gamers, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/game-share');
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
    values.shareTime = convertDateTimeToServer(values.shareTime);

    if (errors.length === 0) {
      const entity = {
        ...gameShareEntity,
        ...values,
        gamer: gamers.find(it => it.id.toString() === values.gamerId.toString()),
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
          <h2 id="gamoLifeApp.gameShare.home.createOrEditLabel" data-cy="GameShareCreateUpdateHeading">
            <Translate contentKey="gamoLifeApp.gameShare.home.createOrEditLabel">Create or edit a GameShare</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : gameShareEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="game-share-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="game-share-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="maxPlayLabel" for="game-share-maxPlay">
                  <Translate contentKey="gamoLifeApp.gameShare.maxPlay">Max Play</Translate>
                </Label>
                <AvField id="game-share-maxPlay" data-cy="maxPlay" type="string" className="form-control" name="maxPlay" />
                <UncontrolledTooltip target="maxPlayLabel">
                  <Translate contentKey="gamoLifeApp.gameShare.help.maxPlay" />
                </UncontrolledTooltip>
              </AvGroup>
              <AvGroup>
                <Label id="shareTimeLabel" for="game-share-shareTime">
                  <Translate contentKey="gamoLifeApp.gameShare.shareTime">Share Time</Translate>
                </Label>
                <AvInput
                  id="game-share-shareTime"
                  data-cy="shareTime"
                  type="datetime-local"
                  className="form-control"
                  name="shareTime"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.gameShareEntity.shareTime)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label for="game-share-gamer">
                  <Translate contentKey="gamoLifeApp.gameShare.gamer">Gamer</Translate>
                </Label>
                <AvInput id="game-share-gamer" data-cy="gamer" type="select" className="form-control" name="gamerId">
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
              <Button tag={Link} id="cancel-save" to="/game-share" replace color="info">
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
  gameShareEntity: storeState.gameShare.entity,
  loading: storeState.gameShare.loading,
  updating: storeState.gameShare.updating,
  updateSuccess: storeState.gameShare.updateSuccess,
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

export default connect(mapStateToProps, mapDispatchToProps)(GameShareUpdate);
