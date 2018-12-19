import { AttendanceRoutingModule } from './attendance-routing.module';

describe('AttendanceRoutingModule', () => {
  let attendanceRoutingModule: AttendanceRoutingModule;

  beforeEach(() => {
    attendanceRoutingModule = new AttendanceRoutingModule();
  });

  it('should create an instance', () => {
    expect(attendanceRoutingModule).toBeTruthy();
  });
});
