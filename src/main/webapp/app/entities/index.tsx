import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Gamer from './gamer';
import PlayHistory from './play-history';
import GameShare from './game-share';
import Game from './game';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}gamer`} component={Gamer} />
      <ErrorBoundaryRoute path={`${match.url}play-history`} component={PlayHistory} />
      <ErrorBoundaryRoute path={`${match.url}game-share`} component={GameShare} />
      <ErrorBoundaryRoute path={`${match.url}game`} component={Game} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
