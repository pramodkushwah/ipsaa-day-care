import { SupportRoutingModule } from './support-routing.module';

describe('SupportRoutingModule', () => {
  let supportRoutingModule: SupportRoutingModule;

  beforeEach(() => {
    supportRoutingModule = new SupportRoutingModule();
  });

  it('should create an instance', () => {
    expect(supportRoutingModule).toBeTruthy();
  });
});
