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
import { getEntity, updateEntity, createEntity, reset } from './play-history.reducer';
import { IPlayHistory } from 'app/shared/model/play-history.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IPlayHistoryUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const PlayHistoryUpdate = (props: IPlayHistoryUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { playHistoryEntity, gamers, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/play-history');
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
        ...playHistoryEntity,
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
          <h2 id="gamoLifeApp.playHistory.home.createOrEditLabel" data-cy="PlayHistoryCreateUpdateHeading">
            <Translate contentKey="gamoLifeApp.playHistory.home.createOrEditLabel">Create or edit a PlayHistory</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : playHistoryEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="play-history-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="play-history-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="maxPlayLabel" for="play-history-maxPlay">
                  <Translate contentKey="gamoLifeApp.playHistory.maxPlay">Max Play</Translate>
                </Label>
                <AvField id="play-history-maxPlay" data-cy="maxPlay" type="string" className="form-control" name="maxPlay" />
                <UncontrolledTooltip target="maxPlayLabel">
                  <Translate contentKey="gamoLifeApp.playHistory.help.maxPlay" />
                </UncontrolledTooltip>
              </AvGroup>
              <AvGroup>
                <Label id="datePlaysLabel" for="play-history-datePlays">
                  <Translate contentKey="gamoLifeApp.playHistory.datePlays">Date Plays</Translate>
                </Label>
                <AvField
                  id="play-history-datePlays"
                  data-cy="datePlays"
                  type="string"
                  className="form-control"
                  name="datePlays"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="playDateLabel" for="play-history-playDate">
                  <Translate contentKey="gamoLifeApp.playHistory.playDate">Play Date</Translate>
                </Label>
                <AvField
                  id="play-history-playDate"
                  data-cy="playDate"
                  type="date"
                  className="form-control"
                  name="playDate"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label for="play-history-gamer">
                  <Translate contentKey="gamoLifeApp.playHistory.gamer">Gamer</Translate>
                </Label>
                <AvInput id="play-history-gamer" data-cy="gamer" type="select" className="form-control" name="gamerId">
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
              <Button tag={Link} id="cancel-save" to="/play-history" replace color="info">
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
  playHistoryEntity: storeState.playHistory.entity,
  loading: storeState.playHistory.loading,
  updating: storeState.playHistory.updating,
  updateSuccess: storeState.playHistory.updateSuccess,
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

export default connect(mapStateToProps, mapDispatchToProps)(PlayHistoryUpdate);
