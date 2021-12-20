import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import PlayHistory from './play-history';
import PlayHistoryDetail from './play-history-detail';
import PlayHistoryUpdate from './play-history-update';
import PlayHistoryDeleteDialog from './play-history-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PlayHistoryUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PlayHistoryUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PlayHistoryDetail} />
      <ErrorBoundaryRoute path={match.url} component={PlayHistory} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={PlayHistoryDeleteDialog} />
  </>
);

export default Routes;
