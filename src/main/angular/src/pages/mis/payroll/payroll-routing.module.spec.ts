import { PayrollRoutingModule } from './payroll-routing.module';

describe('PayrollRoutingModule', () => {
  let payrollRoutingModule: PayrollRoutingModule;

  beforeEach(() => {
    payrollRoutingModule = new PayrollRoutingModule();
  });

  it('should create an instance', () => {
    expect(payrollRoutingModule).toBeTruthy();
  });
});
