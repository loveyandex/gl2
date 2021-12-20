import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntities as getGamers } from 'app/entities/gamer/gamer.reducer';
import { IGame } from 'app/shared/model/game.model';
import { getEntities as getGames } from 'app/entities/game/game.reducer';
import { getEntity, updateEntity, createEntity, reset } from './gamer.reducer';
import { IGamer } from 'app/shared/model/gamer.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IGamerUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const GamerUpdate = (props: IGamerUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { gamerEntity, users, gamers, games, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/gamer');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getUsers();
    props.getGamers();
    props.getGames();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...gamerEntity,
        ...values,
        user: users.find(it => it.id.toString() === values.userId.toString()),
        inviter: gamers.find(it => it.id.toString() === values.inviterId.toString()),
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
          <h2 id="gamoLifeApp.gamer.home.createOrEditLabel" data-cy="GamerCreateUpdateHeading">
            <Translate contentKey="gamoLifeApp.gamer.home.createOrEditLabel">Create or edit a Gamer</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : gamerEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="gamer-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="gamer-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="nameLabel" for="gamer-name">
                  <Translate contentKey="gamoLifeApp.gamer.name">Name</Translate>
                </Label>
                <AvField id="gamer-name" data-cy="name" type="text" name="name" />
              </AvGroup>
              <AvGroup>
                <Label id="phonenumberLabel" for="gamer-phonenumber">
                  <Translate contentKey="gamoLifeApp.gamer.phonenumber">Phonenumber</Translate>
                </Label>
                <AvField
                  id="gamer-phonenumber"
                  data-cy="phonenumber"
                  type="text"
                  name="phonenumber"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="verifyCodeLabel" for="gamer-verifyCode">
                  <Translate contentKey="gamoLifeApp.gamer.verifyCode">Verify Code</Translate>
                </Label>
                <AvField id="gamer-verifyCode" data-cy="verifyCode" type="text" name="verifyCode" />
              </AvGroup>
              <AvGroup>
                <Label id="referalCodeLabel" for="gamer-referalCode">
                  <Translate contentKey="gamoLifeApp.gamer.referalCode">Referal Code</Translate>
                </Label>
                <AvField id="gamer-referalCode" data-cy="referalCode" type="text" name="referalCode" />
              </AvGroup>
              <AvGroup>
                <Label id="scoreLabel" for="gamer-score">
                  <Translate contentKey="gamoLifeApp.gamer.score">Score</Translate>
                </Label>
                <AvField id="gamer-score" data-cy="score" type="string" className="form-control" name="score" />
              </AvGroup>
              <AvGroup check>
                <Label id="canplayGameTodayLabel">
                  <AvInput
                    id="gamer-canplayGameToday"
                    data-cy="canplayGameToday"
                    type="checkbox"
                    className="form-check-input"
                    name="canplayGameToday"
                  />
                  <Translate contentKey="gamoLifeApp.gamer.canplayGameToday">Canplay Game Today</Translate>
                </Label>
              </AvGroup>
              <AvGroup>
                <Label for="gamer-user">
                  <Translate contentKey="gamoLifeApp.gamer.user">User</Translate>
                </Label>
                <AvInput id="gamer-user" data-cy="user" type="select" className="form-control" name="userId">
                  <option value="" key="0" />
                  {users
                    ? users.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="gamer-inviter">
                  <Translate contentKey="gamoLifeApp.gamer.inviter">Inviter</Translate>
                </Label>
                <AvInput id="gamer-inviter" data-cy="inviter" type="select" className="form-control" name="inviterId">
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
              <Button tag={Link} id="cancel-save" to="/gamer" replace color="info">
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
  users: storeState.userManagement.users,
  gamers: storeState.gamer.entities,
  games: storeState.game.entities,
  gamerEntity: storeState.gamer.entity,
  loading: storeState.gamer.loading,
  updating: storeState.gamer.updating,
  updateSuccess: storeState.gamer.updateSuccess,
});

const mapDispatchToProps = {
  getUsers,
  getGamers,
  getGames,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(GamerUpdate);
