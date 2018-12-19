import { MessageRoutingModule } from './message-routing.module';

describe('MessageRoutingModule', () => {
  let messageRoutingModule: MessageRoutingModule;

  beforeEach(() => {
    messageRoutingModule = new MessageRoutingModule();
  });

  it('should create an instance', () => {
    expect(messageRoutingModule).toBeTruthy();
  });
});
