import { CenterRoutingModule } from './center-routing.module';

describe('CenterRoutingModule', () => {
  let centerRoutingModule: CenterRoutingModule;

  beforeEach(() => {
    centerRoutingModule = new CenterRoutingModule();
  });

  it('should create an instance', () => {
    expect(centerRoutingModule).toBeTruthy();
  });
});
