// The file contents for the current environment will overwrite these during build.
// The build system defaults to the dev environment which uses `environment.ts`, but if you do
// `ng build --env=prod` then `environment.prod.ts` will be used instead.
// The list of which env maps to which file can be found in `.angular-cli.json`.

export const environment = {
  production: false,
  api: 'http://ipsaa-dev.ap-south-1.elasticbeanstalk.com' // development backend url
  // api: 'http://localhost:8080'
  // api: 'http://ipsaa-dev2.ap-south-1.elasticbeanstalk.com'
  //  api: window.location.origin
};
