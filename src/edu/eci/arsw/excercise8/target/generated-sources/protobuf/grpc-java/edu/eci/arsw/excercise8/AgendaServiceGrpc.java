package edu.eci.arsw.excercise8;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.63.0)",
    comments = "Source: eciciencia.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class AgendaServiceGrpc {

  private AgendaServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "AgendaService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<edu.eci.arsw.excercise8.TimeSlotRequest,
      edu.eci.arsw.excercise8.ActivityList> getGetActivitiesByTimeSlotMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetActivitiesByTimeSlot",
      requestType = edu.eci.arsw.excercise8.TimeSlotRequest.class,
      responseType = edu.eci.arsw.excercise8.ActivityList.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<edu.eci.arsw.excercise8.TimeSlotRequest,
      edu.eci.arsw.excercise8.ActivityList> getGetActivitiesByTimeSlotMethod() {
    io.grpc.MethodDescriptor<edu.eci.arsw.excercise8.TimeSlotRequest, edu.eci.arsw.excercise8.ActivityList> getGetActivitiesByTimeSlotMethod;
    if ((getGetActivitiesByTimeSlotMethod = AgendaServiceGrpc.getGetActivitiesByTimeSlotMethod) == null) {
      synchronized (AgendaServiceGrpc.class) {
        if ((getGetActivitiesByTimeSlotMethod = AgendaServiceGrpc.getGetActivitiesByTimeSlotMethod) == null) {
          AgendaServiceGrpc.getGetActivitiesByTimeSlotMethod = getGetActivitiesByTimeSlotMethod =
              io.grpc.MethodDescriptor.<edu.eci.arsw.excercise8.TimeSlotRequest, edu.eci.arsw.excercise8.ActivityList>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetActivitiesByTimeSlot"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.eci.arsw.excercise8.TimeSlotRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.eci.arsw.excercise8.ActivityList.getDefaultInstance()))
              .setSchemaDescriptor(new AgendaServiceMethodDescriptorSupplier("GetActivitiesByTimeSlot"))
              .build();
        }
      }
    }
    return getGetActivitiesByTimeSlotMethod;
  }

  private static volatile io.grpc.MethodDescriptor<edu.eci.arsw.excercise8.ActivityRequest,
      edu.eci.arsw.excercise8.ActivityResponse> getGetActivityDetailsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetActivityDetails",
      requestType = edu.eci.arsw.excercise8.ActivityRequest.class,
      responseType = edu.eci.arsw.excercise8.ActivityResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<edu.eci.arsw.excercise8.ActivityRequest,
      edu.eci.arsw.excercise8.ActivityResponse> getGetActivityDetailsMethod() {
    io.grpc.MethodDescriptor<edu.eci.arsw.excercise8.ActivityRequest, edu.eci.arsw.excercise8.ActivityResponse> getGetActivityDetailsMethod;
    if ((getGetActivityDetailsMethod = AgendaServiceGrpc.getGetActivityDetailsMethod) == null) {
      synchronized (AgendaServiceGrpc.class) {
        if ((getGetActivityDetailsMethod = AgendaServiceGrpc.getGetActivityDetailsMethod) == null) {
          AgendaServiceGrpc.getGetActivityDetailsMethod = getGetActivityDetailsMethod =
              io.grpc.MethodDescriptor.<edu.eci.arsw.excercise8.ActivityRequest, edu.eci.arsw.excercise8.ActivityResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetActivityDetails"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.eci.arsw.excercise8.ActivityRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.eci.arsw.excercise8.ActivityResponse.getDefaultInstance()))
              .setSchemaDescriptor(new AgendaServiceMethodDescriptorSupplier("GetActivityDetails"))
              .build();
        }
      }
    }
    return getGetActivityDetailsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<edu.eci.arsw.excercise8.ActivityRequest,
      edu.eci.arsw.excercise8.CapacityResponse> getCheckCapacityMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CheckCapacity",
      requestType = edu.eci.arsw.excercise8.ActivityRequest.class,
      responseType = edu.eci.arsw.excercise8.CapacityResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<edu.eci.arsw.excercise8.ActivityRequest,
      edu.eci.arsw.excercise8.CapacityResponse> getCheckCapacityMethod() {
    io.grpc.MethodDescriptor<edu.eci.arsw.excercise8.ActivityRequest, edu.eci.arsw.excercise8.CapacityResponse> getCheckCapacityMethod;
    if ((getCheckCapacityMethod = AgendaServiceGrpc.getCheckCapacityMethod) == null) {
      synchronized (AgendaServiceGrpc.class) {
        if ((getCheckCapacityMethod = AgendaServiceGrpc.getCheckCapacityMethod) == null) {
          AgendaServiceGrpc.getCheckCapacityMethod = getCheckCapacityMethod =
              io.grpc.MethodDescriptor.<edu.eci.arsw.excercise8.ActivityRequest, edu.eci.arsw.excercise8.CapacityResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CheckCapacity"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.eci.arsw.excercise8.ActivityRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.eci.arsw.excercise8.CapacityResponse.getDefaultInstance()))
              .setSchemaDescriptor(new AgendaServiceMethodDescriptorSupplier("CheckCapacity"))
              .build();
        }
      }
    }
    return getCheckCapacityMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static AgendaServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AgendaServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AgendaServiceStub>() {
        @java.lang.Override
        public AgendaServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AgendaServiceStub(channel, callOptions);
        }
      };
    return AgendaServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static AgendaServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AgendaServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AgendaServiceBlockingStub>() {
        @java.lang.Override
        public AgendaServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AgendaServiceBlockingStub(channel, callOptions);
        }
      };
    return AgendaServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static AgendaServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AgendaServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AgendaServiceFutureStub>() {
        @java.lang.Override
        public AgendaServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AgendaServiceFutureStub(channel, callOptions);
        }
      };
    return AgendaServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void getActivitiesByTimeSlot(edu.eci.arsw.excercise8.TimeSlotRequest request,
        io.grpc.stub.StreamObserver<edu.eci.arsw.excercise8.ActivityList> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetActivitiesByTimeSlotMethod(), responseObserver);
    }

    /**
     */
    default void getActivityDetails(edu.eci.arsw.excercise8.ActivityRequest request,
        io.grpc.stub.StreamObserver<edu.eci.arsw.excercise8.ActivityResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetActivityDetailsMethod(), responseObserver);
    }

    /**
     */
    default void checkCapacity(edu.eci.arsw.excercise8.ActivityRequest request,
        io.grpc.stub.StreamObserver<edu.eci.arsw.excercise8.CapacityResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCheckCapacityMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service AgendaService.
   */
  public static abstract class AgendaServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return AgendaServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service AgendaService.
   */
  public static final class AgendaServiceStub
      extends io.grpc.stub.AbstractAsyncStub<AgendaServiceStub> {
    private AgendaServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AgendaServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AgendaServiceStub(channel, callOptions);
    }

    /**
     */
    public void getActivitiesByTimeSlot(edu.eci.arsw.excercise8.TimeSlotRequest request,
        io.grpc.stub.StreamObserver<edu.eci.arsw.excercise8.ActivityList> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetActivitiesByTimeSlotMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getActivityDetails(edu.eci.arsw.excercise8.ActivityRequest request,
        io.grpc.stub.StreamObserver<edu.eci.arsw.excercise8.ActivityResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetActivityDetailsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void checkCapacity(edu.eci.arsw.excercise8.ActivityRequest request,
        io.grpc.stub.StreamObserver<edu.eci.arsw.excercise8.CapacityResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCheckCapacityMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service AgendaService.
   */
  public static final class AgendaServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<AgendaServiceBlockingStub> {
    private AgendaServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AgendaServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AgendaServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public edu.eci.arsw.excercise8.ActivityList getActivitiesByTimeSlot(edu.eci.arsw.excercise8.TimeSlotRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetActivitiesByTimeSlotMethod(), getCallOptions(), request);
    }

    /**
     */
    public edu.eci.arsw.excercise8.ActivityResponse getActivityDetails(edu.eci.arsw.excercise8.ActivityRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetActivityDetailsMethod(), getCallOptions(), request);
    }

    /**
     */
    public edu.eci.arsw.excercise8.CapacityResponse checkCapacity(edu.eci.arsw.excercise8.ActivityRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCheckCapacityMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service AgendaService.
   */
  public static final class AgendaServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<AgendaServiceFutureStub> {
    private AgendaServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AgendaServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AgendaServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<edu.eci.arsw.excercise8.ActivityList> getActivitiesByTimeSlot(
        edu.eci.arsw.excercise8.TimeSlotRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetActivitiesByTimeSlotMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<edu.eci.arsw.excercise8.ActivityResponse> getActivityDetails(
        edu.eci.arsw.excercise8.ActivityRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetActivityDetailsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<edu.eci.arsw.excercise8.CapacityResponse> checkCapacity(
        edu.eci.arsw.excercise8.ActivityRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCheckCapacityMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_ACTIVITIES_BY_TIME_SLOT = 0;
  private static final int METHODID_GET_ACTIVITY_DETAILS = 1;
  private static final int METHODID_CHECK_CAPACITY = 2;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_ACTIVITIES_BY_TIME_SLOT:
          serviceImpl.getActivitiesByTimeSlot((edu.eci.arsw.excercise8.TimeSlotRequest) request,
              (io.grpc.stub.StreamObserver<edu.eci.arsw.excercise8.ActivityList>) responseObserver);
          break;
        case METHODID_GET_ACTIVITY_DETAILS:
          serviceImpl.getActivityDetails((edu.eci.arsw.excercise8.ActivityRequest) request,
              (io.grpc.stub.StreamObserver<edu.eci.arsw.excercise8.ActivityResponse>) responseObserver);
          break;
        case METHODID_CHECK_CAPACITY:
          serviceImpl.checkCapacity((edu.eci.arsw.excercise8.ActivityRequest) request,
              (io.grpc.stub.StreamObserver<edu.eci.arsw.excercise8.CapacityResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getGetActivitiesByTimeSlotMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              edu.eci.arsw.excercise8.TimeSlotRequest,
              edu.eci.arsw.excercise8.ActivityList>(
                service, METHODID_GET_ACTIVITIES_BY_TIME_SLOT)))
        .addMethod(
          getGetActivityDetailsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              edu.eci.arsw.excercise8.ActivityRequest,
              edu.eci.arsw.excercise8.ActivityResponse>(
                service, METHODID_GET_ACTIVITY_DETAILS)))
        .addMethod(
          getCheckCapacityMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              edu.eci.arsw.excercise8.ActivityRequest,
              edu.eci.arsw.excercise8.CapacityResponse>(
                service, METHODID_CHECK_CAPACITY)))
        .build();
  }

  private static abstract class AgendaServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    AgendaServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return edu.eci.arsw.excercise8.EcicienciaProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("AgendaService");
    }
  }

  private static final class AgendaServiceFileDescriptorSupplier
      extends AgendaServiceBaseDescriptorSupplier {
    AgendaServiceFileDescriptorSupplier() {}
  }

  private static final class AgendaServiceMethodDescriptorSupplier
      extends AgendaServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    AgendaServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (AgendaServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new AgendaServiceFileDescriptorSupplier())
              .addMethod(getGetActivitiesByTimeSlotMethod())
              .addMethod(getGetActivityDetailsMethod())
              .addMethod(getCheckCapacityMethod())
              .build();
        }
      }
    }
    return result;
  }
}
