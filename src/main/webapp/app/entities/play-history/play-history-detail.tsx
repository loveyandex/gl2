import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, UncontrolledTooltip, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './play-history.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPlayHistoryDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const PlayHistoryDetail = (props: IPlayHistoryDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { playHistoryEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="playHistoryDetailsHeading">
          <Translate contentKey="gamoLifeApp.playHistory.detail.title">PlayHistory</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{playHistoryEntity.id}</dd>
          <dt>
            <span id="maxPlay">
              <Translate contentKey="gamoLifeApp.playHistory.maxPlay">Max Play</Translate>
            </span>
            <UncontrolledTooltip target="maxPlay">
              <Translate contentKey="gamoLifeApp.playHistory.help.maxPlay" />
            </UncontrolledTooltip>
          </dt>
          <dd>{playHistoryEntity.maxPlay}</dd>
          <dt>
            <span id="datePlays">
              <Translate contentKey="gamoLifeApp.playHistory.datePlays">Date Plays</Translate>
            </span>
          </dt>
          <dd>{playHistoryEntity.datePlays}</dd>
          <dt>
            <span id="playDate">
              <Translate contentKey="gamoLifeApp.playHistory.playDate">Play Date</Translate>
            </span>
          </dt>
          <dd>
            {playHistoryEntity.playDate ? (
              <TextFormat value={playHistoryEntity.playDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="gamoLifeApp.playHistory.gamer">Gamer</Translate>
          </dt>
          <dd>{playHistoryEntity.gamer ? playHistoryEntity.gamer.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/play-history" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/play-history/${playHistoryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ playHistory }: IRootState) => ({
  playHistoryEntity: playHistory.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PlayHistoryDetail);
