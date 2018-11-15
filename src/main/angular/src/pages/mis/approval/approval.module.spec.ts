import { ApprovalModule } from './approval.module';

describe('ApprovalModule', () => {
  let approvalModule: ApprovalModule;

  beforeEach(() => {
    approvalModule = new ApprovalModule();
  });

  it('should create an instance', () => {
    expect(approvalModule).toBeTruthy();
  });
});
