import { CrmRoutingModule } from './crm-routing.module';

describe('CrmRoutingModule', () => {
  let crmRoutingModule: CrmRoutingModule;

  beforeEach(() => {
    crmRoutingModule = new CrmRoutingModule();
  });

  it('should create an instance', () => {
    expect(crmRoutingModule).toBeTruthy();
  });
});
