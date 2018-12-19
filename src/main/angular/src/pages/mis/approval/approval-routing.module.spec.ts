import { ApprovalRoutingModule } from './approval-routing.module';

describe('ApprovalRoutingModule', () => {
  let approvalRoutingModule: ApprovalRoutingModule;

  beforeEach(() => {
    approvalRoutingModule = new ApprovalRoutingModule();
  });

  it('should create an instance', () => {
    expect(approvalRoutingModule).toBeTruthy();
  });
});
