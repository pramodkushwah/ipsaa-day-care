import { PpModule } from './pp.module';

describe('PpModule', () => {
  let ppModule: PpModule;

  beforeEach(() => {
    ppModule = new PpModule();
  });

  it('should create an instance', () => {
    expect(ppModule).toBeTruthy();
  });
});
